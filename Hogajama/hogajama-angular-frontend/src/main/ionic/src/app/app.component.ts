import {Component} from '@angular/core';

import {Platform} from '@ionic/angular';
import {SplashScreen} from '@ionic-native/splash-screen/ngx';
import {StatusBar} from '@ionic-native/status-bar/ngx';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from './services/AuthenticationService/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html'
})
export class AppComponent {
  public appPages = [
    {
      title: 'Home',
      url: '/home',
      icon: 'home'
    },
    {
      title: 'Login',
      url: '/login',
      icon: 'add-circle-outline'
    },
    {
      title: 'Testing',
      url: '/playground',
      icon: 'bug'
    }
  ];

  constructor(
    private platform: Platform,
    private splashScreen: SplashScreen,
    private statusBar: StatusBar,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthenticationService
  ) {
    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(() => {
      this.statusBar.styleLightContent();
      this.statusBar.backgroundColorByHexString('#2aaf47');
      this.splashScreen.hide();

    });
  }

  async ngOnInit() {
    if (window.location) {
      const params = new URLSearchParams(window.location.hash.substr(1));
      // If we get the params from the keycloak auth back
      if (params.has('state') && params.has('code')) {
        const state = params.get('state'), code = params.get('code');
        console.log('keycloak parameter state: ' + state);
        console.log('keycloak parameter code: ' + code);
      }
    }

    try {
        await this.authService.init().then(() => {
          localStorage.setItem('kc_token', this.authService.getToken());
          localStorage.setItem('kc_refreshToken', this.authService.getRefreshToken());
        });
    } catch (error) {
        console.log('Cant init Keycloak Connection');
    } finally {
        this.router.initialNavigation();
    }
  }
}
