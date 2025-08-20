import { Validators, FormControl } from '@angular/forms';
import { UsuarioService } from 'src/app/services/usuario.service';
import { SolicitacaoFORM } from "./../../../models/SolicitacaoFORM";
import { RelatorioOcorrenciaComponent } from "./../relatorio-ocorrencia/relatorio-ocorrencia.component";
import { RelatorioPontoComponent } from "./../relatorio-ponto/relatorio-ponto.component";
import { AapmService } from "./../../../services/aapm.service";
import { PontoAlunoService } from "./../../../services/ponto-aluno.service";
import { CursoService } from "./../../../services/curso.service";
import { TurmaService } from "src/app/services/turma.service";
import { MatDialog } from "@angular/material/dialog";
import { ToastrService } from "ngx-toastr";
import { Ocorrencia } from "./../../../models/Ocorrencia";
import { OcorrenciaService } from "./../../../services/ocorrencia.service";
import { ActivatedRoute, Router } from "@angular/router";
import { AlunoService } from "src/app/services/aluno.service";
import { Component, OnInit } from "@angular/core";
import { Aluno } from "src/app/models/Aluno";
import { MatTableDataSource } from "@angular/material/table";
import { DialogComponent } from "../../dialog/dialog.component";
import { Location } from "@angular/common";
import { Turma } from "src/app/models/Turma";
import { Curso } from "src/app/models/Curso";
import { PontoAluno } from "src/app/models/PontoAluno";
import { Aapm } from "src/app/models/Aapm";
import { CrachaComponent } from "../cracha/cracha.component";
import { SolicitacaoService } from "src/app/services/solicitacao.service";

export class PontoAlunoTable {
  data: string;
  horario: string;
  acao: string;

  constructor(pontoAluno: PontoAluno) {
    let timestampSplit = pontoAluno.timestamp.split(" ");
    this.data = timestampSplit[0];
    this.horario = timestampSplit[1];
    this.acao = pontoAluno.entradaSaida;
  }
}

@Component({
  selector: "app-aluno-detalhes",
  templateUrl: "./aluno-detalhes.component.html",
  styleUrls: ["./aluno-detalhes.component.css"],
})
export class AlunoDetalhesComponent implements OnInit {
  perfilUsuario : string;
  idAluno: number;
  aluno: Aluno = {
    id: "",
    matricula: null,
    dataMatricula: "",
    dataCriacao: new Date(),
    turma: "",
    numeroTurma: null,
    curso: "",
    nome: "",
    sexo: "",
    idade: "",
    cidade: "",
    rg: "",
    dataNascimento: "",
    email: "",
    telefone: "",
    termoInternet: false,
    internetLiberada: false,
    desbloqueioTemporario: false,
    entradaSaida: "",
    status: "",
    empresa: "",
    tag: null,
    foto: "any",
  };

  turma: Turma = {
    id: "",
    codigo: "",
    curso: "",
    entrada: "",
    saida: "",
    almocoEntrada: "",
    almocoSaida: "",
    toleranciaEntrada: null,
    toleranciaSaida: null,
    periodo: "",
    aulas: [],
    imagem: "",
  };

  curso: Curso = {
    id: "",
    modalidade: "",
    areaTecnologica: "",
    turma: [],
    imagem: "",
  };

  solicitacaoFORM: SolicitacaoFORM = {
    descricao: "",
    status: false,
  };

  bloqOcorrencia: number;

  ELEMENT_DATA_ocorrencia: Ocorrencia[] = [];
  ELEMENT_DATA_aapm: Aapm[] = [];
  ELEMENT_DATA_ponto: PontoAlunoTable[] = [];

  displayedColumns: string[] = [
    "data",
    "tipo",
    "registrado_por",
    "descrição",
    "bloqueio",
    "acao",
  ];

  displayedColumnsPonto: string[] = ["data", "horario", "acao"];

  displayedColumnsAapm: string[] = [
    "data",
    "semestre",
    "recibo",
    "valor",
    "acao",
  ];

  dataSourceOcorrencia = new MatTableDataSource<Ocorrencia>(
    this.ELEMENT_DATA_ocorrencia
  );
  dataSourceAapm = new MatTableDataSource<Aapm>(this.ELEMENT_DATA_aapm);
  dataSourcePonto = new MatTableDataSource<PontoAlunoTable>(
    this.ELEMENT_DATA_ponto
  );

  tag:FormControl = new FormControl(null, Validators.minLength(1))

  constructor(
    private usuarioService: UsuarioService,
    private turmaService: TurmaService,
    private cursoService: CursoService,
    private service: AlunoService,
    private solicitacaoService: SolicitacaoService,
    private toast: ToastrService,
    private ocorrenciaService: OcorrenciaService,
    private aapmService: AapmService,
    private pontoService: PontoAlunoService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.perfilUsuario = this.usuarioService.getPerfilUsuario();
    this.ELEMENT_DATA_ocorrencia = this.route.snapshot.data["ocorrencia"];
    this.idAluno = parseInt(this.route.snapshot.paramMap.get("id"));
    this.findOcorrenciaById();
    this.findById();
    this.findPontoByAlunoId();
    this.findAapmByAlunoId();
  }

  findById(): void {
    this.service.findById(this.idAluno).subscribe(
      (response) => {
        this.aluno = response;
        this.aluno.idade = this.getIdade(this.aluno.dataNascimento);
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

  solicitarCrachaDialog() {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.solicitarCracha();
      }
      return;
    });
  }

  solicitarCracha() {
    this.solicitacaoFORM.descricao = "Impressão de crachá";
    this.solicitacaoFORM.status = false;
    this.solicitacaoService
      .create(this.solicitacaoFORM, this.aluno.id)
      .subscribe(
        (response) => {
          this.toast.success("Solicitação enviada com sucesso!", "Create");
        },
        (ex) => {
          this.toast.error(ex.error.error, "Error");
        }
      );
  }

  solicitarInternetDialog() {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.solicitarInternet();
      }
      return;
    });
  }

  solicitarInternet() {
    this.solicitacaoFORM.descricao = "Liberação de internet da AAPM";
    this.solicitacaoFORM.status = false;
    this.solicitacaoService
      .create(this.solicitacaoFORM, this.aluno.id)
      .subscribe(
        (response) => {
          this.toast.success("Solicitação enviada com sucesso!", "Create");
        },
        (ex) => {
          this.toast.error(ex.error.error, "Error");
        }
      );
  }

  deleteById(): void {
    this.service.deleteById(this.aluno.id).subscribe(
      (response) => {
        this.toast.success("Aluno deletado com sucesso!", "Delete");
        this.location.back();
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  updateTagDialog():void{
    let dialog = this.dialog.open(DialogComponent)
    dialog.afterClosed().subscribe(response=>{
      if(response == 'true'){
        this.updateTag()
      }
      return
    })
  }

  updateTag():void{
    this.service.updateTag(this.aluno.id, this.aluno.tag).subscribe(response=>{
      this.toast.success('Tag editada com sucesso!', 'Update')
      this.findById();
    },(ex)=>{
      this.toast.error(ex.error.error)
    })
  }

  updateDesbloqueioDialog(bloqueio: boolean) {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.updateDesbloqueio(bloqueio);
      } else {
        return;
      }
    });
  }

  updateDesbloqueio(bloqueio: boolean) {
    this.service.updateDesbloqueioTemporario(this.idAluno, bloqueio).subscribe(
      (response) => {
        this.findById();
        this.toast.success("Bloqueio atualizado com sucesso!", "Update");
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  updateTermoInternetDialog(termoInternet: boolean): void {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe(
      (response) => {
        if (response == "true") {
          this.updateTermoInternet(termoInternet);
        }
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  updateTermoInternet(termoInternet: boolean): void {
    this.service.updateTermoInternet(this.aluno.id, termoInternet).subscribe(
      (response) => {
        this.toast.success("Aluno editado com sucesso!", "Update");
        this.findById();
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  updateInternetLiberadaDialog(internetLiberada: boolean): void {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.updateInternetLiberada(internetLiberada);
      }
      return;
    });
  }

  updateInternetLiberada(internetLiberada: boolean): void {
    console.log(internetLiberada);
    this.service
      .updateInternetLiberada(this.aluno.id, internetLiberada)
      .subscribe(
        (response) => {
          this.toast.success("Aluno editado com sucesso!", "Update");
          this.findById();
        },
        (ex) => {
          this.toast.error(ex.error.error, "Error");
        }
      );
  }

  crachaDialog() {
    let dialog = this.dialog.open(CrachaComponent, { data: [this.aluno] });
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
      }
    });
  }

  updateStatusByIdDialog(status: string) {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.updateStatusById(status);
      } else {
        return;
      }
    });
  }

  updateStatusById(status: string) {
    this.aluno.status = status;
    this.findCursoByTurmaCodigo();
    this.service.updateStatus(this.aluno.id, status).subscribe(
      (response) => {
        this.toast.success("Status alterado com sucesso!", "Update");
        this.location.back();
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  findCursoByTurmaCodigo(): void {
    this.cursoService
      .findByTurmaCodigo(this.aluno.turma)
      .subscribe((response) => {
        this.curso = response;
      });
  }

  findPontoByAlunoId(): void {
    this.pontoService.findAllByAlunoId(this.idAluno).subscribe(
      (response) => {
        response.forEach((element) => {
          this.ELEMENT_DATA_ponto.push(new PontoAlunoTable(element));
        });
        this.dataSourcePonto = new MatTableDataSource<PontoAlunoTable>(
          this.ELEMENT_DATA_ponto
        );
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  findOcorrenciaById(): void {
    this.ocorrenciaService.findAllByAlunoId(this.idAluno).subscribe(
      (response) => {
        this.ELEMENT_DATA_ocorrencia = response;
        this.bloqOcorrencia = this.ELEMENT_DATA_ocorrencia.filter(
          (ocorrencia) => {
            return ocorrencia.bloqueio == true;
          }
        ).length;
        this.dataSourceOcorrencia = new MatTableDataSource<Ocorrencia>(
          this.ELEMENT_DATA_ocorrencia
        );
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  findAapmByAlunoId(): void {
    this.aapmService.findAllByAluno(this.idAluno).subscribe(
      (response) => {
        this.ELEMENT_DATA_aapm = response;
        this.dataSourceAapm = new MatTableDataSource<Aapm>(
          this.ELEMENT_DATA_aapm
        );
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  OcorrenciaDeleteDialog(id: number) {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.OcorrenciaDelete(id);
      } else {
        return;
      }
    });
  }

  OcorrenciaDelete(id: number) {
    this.ocorrenciaService.delete(id).subscribe(
      (response) => {
        this.toast.success("Ocorrencia deletada com sucesso!", "Delete");
        this.findOcorrenciaById();
      },
      (ex) => {
        if (ex.status === 403) {
          this.toast.error(
            "Você não tem autorização para essa operação",
            "Error"
          );
          return;
        }
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  pontoToPdf(): void {
    if (this.dataSourcePonto.filteredData.length < 1) {
      this.toast.error("Aluno sem ponto registrado!", "Error");
      return;
    }
    let dialog = this.dialog.open(RelatorioPontoComponent, {
      data: [this.dataSourcePonto.filteredData, this.aluno],
    });
    dialog.afterClosed().subscribe((response) => {
      if (response) {
        return;
      }
    });
  }

  ocorrenciaToPdf(): void {
    if (this.dataSourceOcorrencia.filteredData.length < 1) {
      this.toast.error("Aluno sem ocorrências!", "Error");
      return;
    }
    let dialog = this.dialog.open(RelatorioOcorrenciaComponent, {
      data: [this.dataSourceOcorrencia.filteredData, this.aluno],
    });
    dialog.afterClosed().subscribe((response) => {
      if (response) {
        return;
      }
    });
  }

  AapmDeleteDialog(id: number): void {
    let dialog = this.dialog.open(DialogComponent);
    dialog.afterClosed().subscribe((response) => {
      if (response == "true") {
        this.AapmDelete(id);
      } else {
        return;
      }
    });
  }

  AapmDelete(id: number): void {
    this.aapmService.delete(id).subscribe(
      (response) => {
        this.toast.success("Pagamento da Aapm deletado com sucesso!", "Delete");
        this.findAapmByAlunoId();
      },
      (ex) => {
        this.toast.error(ex.error.error, "Error");
      }
    );
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceOcorrencia.filter = filterValue.trim().toLowerCase();
  }

  applyFilterPonto(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourcePonto.filter = filterValue.trim().toLowerCase();
  }

  applyFilterAapm(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSourceAapm.filter = filterValue.trim().toLowerCase();
  }

  validaCampos():boolean{
    return this.tag.valid
  }

  getIdade(birthdate: string): string {
    let birthdateSplit = birthdate.split("-");
    const today = new Date().toLocaleDateString("en-CA").split("-");
    let yearsDiff = parseInt(today[0]) - parseInt(birthdateSplit[0]);
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
