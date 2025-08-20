import { UsuarioService } from 'src/app/services/usuario.service';
import { CrachaFuncionarioComponent } from "./../cracha-funcionario/cracha-funcionario.component";
import { RelatorioPontoFuncionarioComponent } from "./../relatorio-ponto-funcionario/relatorio-ponto-funcionario.component";
import { PontoFuncionarioService } from "./../../../services/ponto-funcionario.service";
import { MatDialog } from "@angular/material/dialog";
import { ToastrService } from "ngx-toastr";
import { ActivatedRoute } from "@angular/router";
import { FuncionarioService } from "./../../../services/funcionario.service";
import { Component, OnInit } from "@angular/core";
import { Funcionario } from "src/app/models/Funcionario";
import { PontoFuncionario } from "src/app/models/PontoFuncionario";
import { MatTableDataSource } from "@angular/material/table";
import { DialogComponent } from "../../dialog/dialog.component";
import { Location } from "@angular/common";

export class PontoFuncionarioTable {
  data: string;
  horario: string;
  acao: string;

  constructor(pontoAluno: PontoFuncionario) {
    let timestampSplit = pontoAluno.timestamp.split(" ");
    this.data = timestampSplit[0];
    this.horario = timestampSplit[1];
    this.acao = pontoAluno.entradaSaida;
  }
}
@Component({
  selector: "app-funcionario-detalhes",
  templateUrl: "./funcionario-detalhes.component.html",
  styleUrls: ["./funcionario-detalhes.component.css"],
})
export class FuncionarioDetalhesComponent implements OnInit {
  perfilUsuario:string;
  id: number = null;
  funcionario: Funcionario = {
    id: null,
    nome: "",
    sexo: "",
    idade: "",
    rg: "",
    email: "",
    telefone: "",
    cidade: "",
    dataNascimento: "",
    tag: null,
    entradaSaida: "LIVRE_ACESSO",
    matricula: null,
    empresa: "",
    foto: "",
  };

  ELEMENT_DATA_ponto: PontoFuncionarioTable[] = [];

  displayedColumnsPonto: string[] = ["data", "horario", "acao"];

  dataSourcePonto = new MatTableDataSource<PontoFuncionarioTable>(
    this.ELEMENT_DATA_ponto
  );
  constructor(
    private service: FuncionarioService,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private toast: ToastrService,
    private dialog: MatDialog,
    private location: Location,
    private pontoService: PontoFuncionarioService
  ) {}

  ngOnInit(): void {
    this.perfilUsuario = this.usuarioService.getPerfilUsuario(); 
    this.id = parseInt(this.route.snapshot.paramMap.get("id"));
    this.findById();
    this.findPontoByFuncionarioId();
  }
  findById(): void {
    this.service.findById(this.id).subscribe(
      (response) => {
        this.funcionario = response;
        this.funcionario.idade = this.getIdade(this.funcionario.dataNascimento);
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  findPontoByFuncionarioId(): void {
    this.pontoService.findAllByFuncionarioId(this.id).subscribe(
      (response) => {
        response.forEach((element) => {
          this.ELEMENT_DATA_ponto.push(new PontoFuncionarioTable(element));
        });
        this.dataSourcePonto = new MatTableDataSource<PontoFuncionarioTable>(
          this.ELEMENT_DATA_ponto
        );
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  deleteByIdDialog() {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.deleteById();
      } else {
        return;
      }
    });
  }

  deleteById(): void {
    this.service.deleteById(this.id).subscribe(
      (response) => {
        this.toast.success("Funcionário deletado com sucesso!", "Delete");
        this.location.back();
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  crachaDialog() {
    let dialog = this.dialog.open(CrachaFuncionarioComponent, {
      data: [this.funcionario],
    });
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
      }
    });
  }

  pontoToPdf(): void {
    if (this.dataSourcePonto.filteredData.length < 1) {
      this.toast.error("Funcionário sem ponto registrado!", "Error");
      return;
    }
    let dialog = this.dialog.open(RelatorioPontoFuncionarioComponent, {
      data: [this.dataSourcePonto.filteredData, this.funcionario],
    });
    dialog.afterClosed().subscribe((response) => {
      if (response) {
        return;
      }
    });
  }

  applyFilterPonto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourcePonto.filter = filterValue.trim().toLowerCase();
  }

  getIdade(birthdate: string): string {
    let birthdateSplit = birthdate.split("-");
    const today = new Date().toLocaleDateString("en-CA").split("-");
    let yearsDiff = parseInt(today[0]) - parseInt(birthdateSplit[0]);
    console.log(parseInt(today[1]));
    console.log(parseInt(birthdateSplit[1]));
    if (
      parseInt(today[1]) > parseInt(birthdateSplit[1]) ||
      (parseInt(today[1]) == parseInt(birthdateSplit[1]) &&
        parseInt(today[2]) >= parseInt(birthdateSplit[2]))
    ) {
      return yearsDiff.toString();
    }
    return (yearsDiff - 1).toString();
  }
}
