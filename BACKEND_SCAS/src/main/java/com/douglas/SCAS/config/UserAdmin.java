package com.douglas.SCAS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.douglas.SCAS.enums.TipoPerfil;
import com.douglas.SCAS.model.Perfil;
import com.douglas.SCAS.model.Usuario;
import com.douglas.SCAS.service.UsuarioService;

@Configuration
public class UserAdmin implements ApplicationRunner {

	@Value("${userAdmin.username}")
	private String adminUsername;

	@Value("${userAdmin.password}")
	private String adminPassword;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		Usuario usuarioAdmin = new Usuario(this.adminUsername, this.adminPassword, "Perfil Admininstrador default",
				"Admin");
		Perfil perfil = new Perfil(TipoPerfil.ADMIN);
		usuarioAdmin.addPerfis(perfil);
		try {
			usuarioService.create(usuarioAdmin);
			System.out.println("Usuário admin criado!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
}
