package org.example.usuarioInterface;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.dao.UsuarioService;
import org.example.entities.Usuario;
import org.example.simulationV1.simulation.ProcessamentoCriaturas;
import org.example.simulationV1.simulation.RespostaProcessamento;

import java.util.List;

import java.util.Scanner;

public class UsuarioInterface {
    private final Scanner sc;

    public UsuarioInterface(Scanner scanner) {
        this.sc = scanner;
    }

    public void iniciar(){
        boolean shouldRun = true;
        long usuarioAtualId = 0;
        boolean shouldLogIn = false;
        while(shouldRun){
            if(shouldLogIn){
                System.out.println("Digite o número da operação: ");
                System.out.println("1 - Criar simulação");
                System.out.println("2 - Listar usuarios");
                System.out.println("3 - Sair");

                int op = Integer.parseInt(sc.nextLine());
                switch(op){
                    case 1:
                        executarSimulacao(usuarioAtualId);
                        break;
                    case 2:
                        listarUsuarios();
                        break;
                    case 3:
                        shouldLogIn = false;
                        break;
                }
            }
            else {
                System.out.println("Digite o número da operação: ");
                System.out.println("1 - Fazer Login");
                System.out.println("2 - Fazer cadastro de usuario");
                System.out.println("3 - Sair");

                int op = Integer.parseInt(sc.nextLine());
                switch (op) {
                    case 1:
                        usuarioAtualId = login();
                        if(usuarioAtualId != 0){
                            shouldLogIn = true;
                        }
                        break;
                    case 2:
                        Usuario u = salvarUsuario();
                        System.out.println(u);
                        break;
                    case 3:
                        shouldRun = false;
                        break;
                }
            }
        }
    }

    public void executarSimulacao(Long usuarioId){
        System.out.println("Informe a quantidade de criaturas (MAX=199):");
        int quantCriaturas = sc.nextInt();
        System.out.println("Informe o tempo de execução da simulação (em segundos): ");
        int tempoExecucao = sc.nextInt();

        RespostaProcessamento resposta = ProcessamentoCriaturas.processamento(quantCriaturas,tempoExecucao);

        UsuarioService usuarioService = new UsuarioService();

            Usuario usuario = usuarioService.buscarPorId(usuarioId);
            usuario.setQuantidadeSimulacoes(usuario.getQuantidadeSimulacoes() + 1);

            if(resposta.getStatus()==1){
                usuario.setQuantidadeSimulacoesBemSucedidas(
                        usuario.getQuantidadeSimulacoesBemSucedidas() + 1
                );
            }


            usuario.setMediaSimulacoesBemSucedidas(
                    (float) usuario.getQuantidadeSimulacoesBemSucedidas()/usuario.getQuantidadeSimulacoes()
            );

            usuarioService.salvarUsuario(usuario);
            System.out.println("Simulação realizada com sucesso!");
            sc.nextLine();
    }

    public Usuario salvarUsuario(){
        System.out.println("Informe o login do usuario: ");
        String login = sc.nextLine();
        System.out.println("Informe a senha do usuario: ");
        String senha = sc.nextLine();
        System.out.println("Informe o avatar do usuario: ");
        String avatar = sc.nextLine();

        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setAvatar(avatar);
        usuario.setQuantidadeSimulacoes(0);
        usuario.setMediaSimulacoesBemSucedidas(0F);
        usuario.setQuantidadeSimulacoesBemSucedidas(0);
        usuario.setPontuacao(0D);


        UsuarioService usuarioService = new UsuarioService();

        Usuario usuarioEncontrado = new Usuario();
        return usuarioService.salvarUsuario(usuario);
    }


    public long login(){
        System.out.println("Informe o login do usuario: ");
        String login = sc.nextLine();
        System.out.println("Informe a senha do usuario: ");
        String senha = sc.nextLine();

        UsuarioService usuarioService = new UsuarioService();

            Usuario usuarioEncontrado = usuarioService.buscarPorLogin(login);
            if(usuarioEncontrado.getSenha().equals(senha)){
                System.out.println("Login realizado com sucesso!");
                return usuarioEncontrado.getId();
            }
            else return 0;
    }

    public void listarUsuarios(){
        UsuarioService usuarioService = new UsuarioService();

        List<Usuario> usuarios = usuarioService.buscarTodos();
        usuarios.forEach(System.out::println);

        System.out.println("Quantidade total de simulações = "
                +usuarios.stream().mapToInt(Usuario::getQuantidadeSimulacoes).sum());
        System.out.println("Média de simulações bem sucedidas totais = "
                +usuarios.stream().mapToDouble(Usuario::getQuantidadeSimulacoesBemSucedidas).sum()/
                usuarios.stream().mapToDouble(Usuario::getQuantidadeSimulacoes).sum());

    }

}
