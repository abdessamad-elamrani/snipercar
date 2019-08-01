import { Component, OnInit } from '@angular/core';

interface FsDocument extends HTMLDocument {
  webkitExitFullscreen: any;
  webkitFullscreenElement: Element;
  mozFullScreenElement?: Element;
  msFullscreenElement?: Element;
  msExitFullscreen?: () => void;
  mozCancelFullScreen?: () => void;
}

interface FsDocumentElement extends HTMLElement {
  webkitRequestFullscreen: any;
  msRequestFullscreen?: () => void;
  mozRequestFullScreen?: () => void;
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent implements OnInit {

  public scrollbarOptions = {
    axis: 'y',
    theme: 'minimal',
  };

  constructor() { }

  ngOnInit() {
  }

  mouseEnter(event: any) {
    if (event.type == 'click') {
      const target = event.srcElement.className;
      if (target != '') {
        const $li = $('#' + target);
        $li.children('.child_menu').css({ opacity: '1', visibility: 'visible', animation: 'slideleft 0.3s' });
      }
    } else {
      const target = event.srcElement.id;
      const $li = $('#' + target);
      $li.children('.child_menu').css({ opacity: '1', visibility: 'visible', animation: 'slideleft 0.3s' });
    }
  }

  mouseLeave(event: any) {
    const target = event.srcElement.id;
    const $li = $('#' + target);
    $li.children('.child_menu').css({ opacity: '0', visibility: 'hidden', animation: 'slideRight 0.3s' });
  }

  fullScreen(): void {
    const fsDoc = document as FsDocument;
    if (!this.isFullScreen()) {
      const fsDocElem = document.documentElement as FsDocumentElement;
      if (fsDocElem.requestFullscreen) {
        fsDocElem.requestFullscreen();
      } else if (fsDocElem.msRequestFullscreen) {
        fsDocElem.msRequestFullscreen();
      } else if (fsDocElem.mozRequestFullScreen) {
        fsDocElem.mozRequestFullScreen();
      } else if (fsDocElem.webkitRequestFullscreen) {
        fsDocElem.webkitRequestFullscreen();
      }
    } else if (fsDoc.exitFullscreen) {
      fsDoc.exitFullscreen();
    } else if (fsDoc.msExitFullscreen) {
      fsDoc.msExitFullscreen();
    } else if (fsDoc.mozCancelFullScreen) {
      fsDoc.mozCancelFullScreen();
    } else if (fsDoc.webkitExitFullscreen) {
      fsDoc.webkitExitFullscreen();
    }
  }

  setFullScreen(full: boolean): void {
    if (full !== this.isFullScreen()) {
      this.isFullScreen();
    }
  }

  isFullScreen(): boolean {
    const fsDoc = document as FsDocument;
    return !!(fsDoc.fullscreenElement || fsDoc.mozFullScreenElement || fsDoc.webkitFullscreenElement || fsDoc.msFullscreenElement);
  }


}
