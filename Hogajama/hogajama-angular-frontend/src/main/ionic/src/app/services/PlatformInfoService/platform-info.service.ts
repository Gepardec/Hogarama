import {Injectable} from '@angular/core';
import {DeviceDetectorService} from "ngx-device-detector";

@Injectable({
  providedIn: 'root'
})
export class PlatformInfoService {
  private userAgent: string;

  constructor(private deviceService: DeviceDetectorService) {
    this.userAgent = navigator.userAgent.toLowerCase();
  }

  public isCurrentPlatformElectron(): boolean {
    return (this.userAgent.indexOf(' electron/') > - 1);
  }

  public isCurrentPlatformApp(): boolean {
    return !!(<any>window).cordova;
  }

  public isCurrentPlatformNativeMobile(): boolean {
    return this.deviceService.isMobile();
  }

  public isCurrentPlatformMobile(): boolean {
    return this.isCurrentPlatformApp() || this.isCurrentPlatformNativeMobile() || (window.innerWidth * 1.5 < window.innerHeight) || (window.innerWidth < 800);
  }
}
