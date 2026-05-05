import {
  AngularNodeAppEngine,
  createNodeRequestHandler,
  isMainModule,
  writeResponseToNodeResponse,
} from '@angular/ssr/node';
import express from 'express';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');
const backendUrl = process.env['BACKEND_URL'] || 'http://localhost:8080';

const app = express();
const angularApp = new AngularNodeAppEngine();

const proxiedPrefixes = [
  '/api',
  '/auth',
  '/students',
  '/faculty',
  '/departments',
  '/course',
  '/subjects',
  '/attendance',
  '/timetables',
  '/facultySchedule',
  '/exams'
];

app.use(async (req, res, next) => {
  const matchesProxy = proxiedPrefixes.some(prefix => req.path.startsWith(prefix));
  if (!matchesProxy) {
    next();
    return;
  }

  try {
    const targetUrl = new URL(req.originalUrl, backendUrl);
    const headers = new Headers();

    for (const [key, value] of Object.entries(req.headers)) {
      if (value === undefined || key.toLowerCase() === 'host') {
        continue;
      }
      if (Array.isArray(value)) {
        value.forEach(entry => headers.append(key, entry));
      } else {
        headers.set(key, value);
      }
    }

    let body: string | undefined;
    if (!['GET', 'HEAD'].includes(req.method.toUpperCase())) {
      body = await new Promise<string>((resolveBody, rejectBody) => {
        let data = '';
        req.setEncoding('utf8');
        req.on('data', chunk => { data += chunk; });
        req.on('end', () => resolveBody(data));
        req.on('error', rejectBody);
      });
    }

    const response = await fetch(targetUrl, {
      method: req.method,
      headers,
      body
    });

    res.status(response.status);
    response.headers.forEach((value, key) => {
      if (key.toLowerCase() === 'content-encoding') {
        return;
      }
      res.setHeader(key, value);
    });

    const buffer = Buffer.from(await response.arrayBuffer());
    res.send(buffer);
  } catch (error) {
    next(error);
  }
});

/**
 * Serve static files from /browser
 */
app.use(
  express.static(browserDistFolder, {
    maxAge: '1y',
    index: false,
    redirect: false,
  }),
);

/**
 * Handle all other requests by rendering the Angular application.
 */
app.use('/**', (req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});

/**
 * Start the server if this module is the main entry point.
 * The server listens on the port defined by the `PORT` environment variable, or defaults to 4000.
 */
if (isMainModule(import.meta.url)) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
  });
}

/**
 * Request handler used by the Angular CLI (for dev-server and during build) or Firebase Cloud Functions.
 */
export const reqHandler = createNodeRequestHandler(app);
