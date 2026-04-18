import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule} from '@angular/router';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { LandingComponent } from './components/layout/landing/landing.component';
import { CoreRoutingModule } from './core-routing.module';

@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    LandingComponent
  ],
  imports: [
    RouterModule,
    CommonModule,
    CoreRoutingModule
  ],
  exports: [
    HeaderComponent,
    FooterComponent,
    LandingComponent
  ]
})
export class CoreModule {}