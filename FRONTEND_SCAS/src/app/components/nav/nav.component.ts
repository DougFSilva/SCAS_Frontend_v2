import { UsuarioService } from 'src/app/services/usuario.service';
import { AlunoService } from "src/app/services/aluno.service";
import { MatDialog } from "@angular/material/dialog";
import { ChangeDetectorRef, Component, HostListener, OnInit, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { AuthService } from "src/app/services/auth.service";
import { DialogComponent } from "../dialog/dialog.component";
import { UploadFilesComponent } from "../upload-files/upload-files.component";
import { Subject } from "rxjs";
import { debounceTime } from "rxjs";
import { MatDrawer } from '@angular/material/sidenav';

@Component({
  selector: "app-nav",
  templateUrl: "./nav.component.html",
  styleUrls: ["./nav.component.css"],
})
export class NavComponent implements OnInit {

  @ViewChild('drawer') drawer!: MatDrawer;
  mode: 'side' | 'over' = 'side';
  usuario: string;
  perfilUsuario: string;
  panelOpenState = false;
  showFiller = true;
  alunoTag: number = null;
  debounce: Subject<number> = new Subject<number>();

  constructor(
    private router: Router,
    private authService: AuthService,
    private toast: ToastrService,
    private dialog: MatDialog,
    private alunoService: AlunoService,
    private usuarioService: UsuarioService,
  ) {}

  @HostListener('window:resize', [])
  onResize() {
    const screenWidth = window.innerWidth;
    this.mode = screenWidth < 768 ? 'over' : 'side';
    if (screenWidth < 768) {
      this.drawer.close();
    } else {
      this.drawer.open();
    }
  }

  ngOnInit(): void {
    if (this.router.url === "/home") {
      this.router.navigate(["home"]);
    }
    this.usuario = this.usuarioService.getUsuario();
    this.perfilUsuario = this.usuarioService.getPerfilUsuario();
    this.debounce.pipe(debounceTime(300)).subscribe(() => {
      this.alunoService.findByTag(this.alunoTag).subscribe(
        (response) => {
          this.router.navigate([`/aluno/${response.id}`], {});
        },
        (ex) => {
          this.toast.error(ex.error.error, "Error");
        }
      );
    });
  }

  ngAfterViewInit(): void {
    this.onResize();
  }

  logoutDialog() {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.logout();
      } else {
        return;
      }
    });
  }

  logout() {
    this.router.navigate(["login"]);
    this.authService.logout();
    this.toast.info("Logout realizado com sucesso", "Logout", {
      timeOut: 7000,
    });
  }

  imageUpload(): void {
    let dialog = this.dialog.open(UploadFilesComponent, {
      data: { folder: "login", name: `login_background` },
    });
    dialog.afterClosed().subscribe((response) => {
      if (response) {
        return;
      }
    });
  }

  findAlunoByTag() {
    if (this.alunoTag) {
      this.debounce.next(this.alunoTag);
      return;
    }
    return;
  }

  navigateAndClose(route: string) {
    this.router.navigate([route]).then(() => {
      if (window.innerWidth < 768) {
        this.drawer.close();
      }
    });
  }
}
