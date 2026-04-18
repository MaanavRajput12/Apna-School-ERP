
export default {
  bootstrap: () => import('./main.server.mjs').then(m => m.default),
  inlineCriticalCss: true,
  baseHref: '/',
  locale: undefined,
  routes: [
  {
    "renderMode": 2,
    "route": "/"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-DQ4CRW5P.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/auth"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-QZEFS3FJ.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/students"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-QZEFS3FJ.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/students/attendance"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-QZEFS3FJ.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/students/exam"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-QZEFS3FJ.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/students/fees"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-QZEFS3FJ.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/students/timetable"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/students"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/faculty"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/studenttimetable"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/facultytimetable"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/studentfees"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-RQA6WFXV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/admin/examtimetable"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-7JTDI3YV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/faculty"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-7JTDI3YV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/faculty/attendance"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-7JTDI3YV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/faculty/faculty-schedule"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-7JTDI3YV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/faculty/student-schedule"
  },
  {
    "renderMode": 2,
    "preload": [
      "chunk-7JTDI3YV.js",
      "chunk-AHRO5KEO.js",
      "chunk-DX534KXM.js"
    ],
    "route": "/faculty/exam-schedule"
  }
],
  entryPointToBrowserMapping: undefined,
  assets: {
    'index.csr.html': {size: 963, hash: '983b6331d389a606132373b72b19f75ec2a501a822ccf8b9cc44309903916f80', text: () => import('./assets-chunks/index_csr_html.mjs').then(m => m.default)},
    'index.server.html': {size: 1114, hash: '59b5f6560410eb297514f70831d3d17625207985fbbda6d49de78e3ed495d14e', text: () => import('./assets-chunks/index_server_html.mjs').then(m => m.default)},
    'index.html': {size: 9285, hash: '4fa0ef77792afe50f56cb090b4de86ee6f6c1ad08e81831f075fa91a5e443653', text: () => import('./assets-chunks/index_html.mjs').then(m => m.default)},
    'students/exam/index.html': {size: 8305, hash: '596950d28012f6aba7ec8de01c8c9c3b269fa6c490e2803bb303ab2b80d9627e', text: () => import('./assets-chunks/students_exam_index_html.mjs').then(m => m.default)},
    'students/fees/index.html': {size: 7828, hash: '0ebe4a1a61d02d925dfa545b4798b764b641211494cd24660108c88dd0fd3d36', text: () => import('./assets-chunks/students_fees_index_html.mjs').then(m => m.default)},
    'students/timetable/index.html': {size: 8336, hash: '75be9ed13dcb26cff7d5ab8b70a0fbf42e5420a2a380741e5b3dab36e9b43154', text: () => import('./assets-chunks/students_timetable_index_html.mjs').then(m => m.default)},
    'auth/index.html': {size: 9527, hash: '380c183f65539c58a7995348268d7f6995953a3ecc08f9f12eb4652ece598812', text: () => import('./assets-chunks/auth_index_html.mjs').then(m => m.default)},
    'students/attendance/index.html': {size: 8402, hash: '50f1b778e3ee24bcb9a20a7057925ec655d6f193c5ba36423804afab7b821cea', text: () => import('./assets-chunks/students_attendance_index_html.mjs').then(m => m.default)},
    'admin/index.html': {size: 8996, hash: '7c9d8ac4175ae48b0fe15a98edc853661267c360e8242877386b63d7c8a85c8a', text: () => import('./assets-chunks/admin_index_html.mjs').then(m => m.default)},
    'admin/students/index.html': {size: 12052, hash: '751a8103915dee2d5c23ef4362f44256502a9910701b3dada7eb25858d0bd7fe', text: () => import('./assets-chunks/admin_students_index_html.mjs').then(m => m.default)},
    'students/index.html': {size: 9607, hash: 'd3ad35b12379b9c820d6d3b4bdf60d9b4bd63c622a25fc8d2715fb63cbd1231c', text: () => import('./assets-chunks/students_index_html.mjs').then(m => m.default)},
    'admin/faculty/index.html': {size: 9827, hash: 'd74695c752b7b4ffb3e12f5cc38405205e1319976f42d678cc505b9be7d131e2', text: () => import('./assets-chunks/admin_faculty_index_html.mjs').then(m => m.default)},
    'admin/studenttimetable/index.html': {size: 8871, hash: '0b56ad2c6b6833daeafef7af2cc4f3796ef2cb6b1db95b64c67ebe2ceb754a8d', text: () => import('./assets-chunks/admin_studenttimetable_index_html.mjs').then(m => m.default)},
    'admin/facultytimetable/index.html': {size: 8879, hash: 'f0df65041c433b0a6051e878ad59081f9cc6e4474f3ffdcb70674189bd9869f4', text: () => import('./assets-chunks/admin_facultytimetable_index_html.mjs').then(m => m.default)},
    'admin/studentfees/index.html': {size: 7781, hash: '2d0c1dc2e5aecf785e5c8df774b61ff3db9b894b1683d1eba1de74e9e8b84993', text: () => import('./assets-chunks/admin_studentfees_index_html.mjs').then(m => m.default)},
    'faculty/index.html': {size: 8925, hash: '37b37256fe437248bc1094557526ecd89d75685a338440233f9f85d858617a8e', text: () => import('./assets-chunks/faculty_index_html.mjs').then(m => m.default)},
    'admin/examtimetable/index.html': {size: 9464, hash: '10eb55a768cf7253a23b64212ed9491f341b992c6c300a82340c40a5d4d8cbe8', text: () => import('./assets-chunks/admin_examtimetable_index_html.mjs').then(m => m.default)},
    'faculty/exam-schedule/index.html': {size: 8342, hash: '96696ad2cbea4d5861631fdaaeea0669e423f46d3221d6dfee90feaccf1437e2', text: () => import('./assets-chunks/faculty_exam-schedule_index_html.mjs').then(m => m.default)},
    'faculty/attendance/index.html': {size: 9000, hash: '027e7b59471230cbebba47aa70c93768500bfec1045663bd47efb0f1273628ba', text: () => import('./assets-chunks/faculty_attendance_index_html.mjs').then(m => m.default)},
    'faculty/faculty-schedule/index.html': {size: 8342, hash: '90e662e3850dacf899e9740ef63aeab121f9e0edb8cf50ba31a72339863bcfc1', text: () => import('./assets-chunks/faculty_faculty-schedule_index_html.mjs').then(m => m.default)},
    'faculty/student-schedule/index.html': {size: 8342, hash: 'f489e6989861c8cc323685c5564827951cc0638f72f8ffdf5ebe7a712f898e6e', text: () => import('./assets-chunks/faculty_student-schedule_index_html.mjs').then(m => m.default)},
    'styles-ZLYFI4LI.css': {size: 3745, hash: '8O7bFM9TkIE', text: () => import('./assets-chunks/styles-ZLYFI4LI_css.mjs').then(m => m.default)}
  },
};
