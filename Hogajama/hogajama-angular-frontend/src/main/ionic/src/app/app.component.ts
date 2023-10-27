import {Component} from '@angular/core';

import {Platform, LoadingController} from '@ionic/angular';
import {SplashScreen} from '@awesome-cordova-plugins/splash-screen/ngx';
import {StatusBar} from '@awesome-cordova-plugins/status-bar/ngx';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from './services/AuthenticationService/authentication.service';
import {HogaramaBackendService} from './services/HogaramaBackendService/hogarama-backend.service';

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
    },
    {
      title: 'Chat',
      url: '/chat',
      icon: 'chatbox-ellipses-outline'
    }
  ];

  constructor(
    private platform: Platform,
    private splashScreen: SplashScreen,
    private statusBar: StatusBar,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthenticationService,
    private rs: HogaramaBackendService,
    private loadingController: LoadingController
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
      const params = new URLSearchParams(window.location.hash.substring(1));
      // If we get the params from the keycloak auth back
      if (params.has('state') && params.has('code')) {
        const state = params.get('state'), code = params.get('code');
        console.info('keycloak parameter state: ', state);
        console.info('keycloak parameter code: ', code);
      }
    }

    const loading = await this.loadingController.create({
      message: 'Initialize Security...'
    });
    this.rs.clientConfig.getByBearer()
      .then((config) => this.authService.init(config))
      .finally(() => {
        loading.dismiss()
        this.router.initialNavigation()
      })
    await loading.present();
  }

}
