package com.soulcode.chamaelas.ChamaElas.services;


import com.soulcode.chamaelas.ChamaElas.models.ChamadoModel;
import com.soulcode.chamaelas.ChamaElas.models.ClienteModel;
import com.soulcode.chamaelas.ChamaElas.models.FuncaoModel;
import com.soulcode.chamaelas.ChamaElas.models.UsuarioModel;
import com.soulcode.chamaelas.ChamaElas.repositories.ChamadoRepository;
import com.soulcode.chamaelas.ChamaElas.repositories.FuncaoRepository;
import com.soulcode.chamaelas.ChamaElas.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FuncaoRepository funcaoRepository;

    public void cadastrarNovoUsuario(String nome, String email, String senha, String confirmacaoSenha, String funcao, Model model) {
        autenticacaoService.verificarCadastroUsuario(nome, email, senha, confirmacaoSenha);
        var funcaoModel = obterOuCriarFuncao(funcao);
        var usuarioModel = criarUsuario(nome, email, senha, funcaoModel);
        usuarioRepository.save(usuarioModel);

        model.addAttribute("successMessage", "Usuário cadastrado com sucesso! Faça o login para acessar sua conta.");
    }

    private FuncaoModel obterOuCriarFuncao(String nomeFuncao) {
        FuncaoModel funcaoModel;
        switch (nomeFuncao) {
            case "Cliente":
                funcaoModel = funcaoRepository.findByNome(FuncaoModel.Values.CLIENTE.getNome());
                if (funcaoModel == null) {
                    funcaoModel = funcaoRepository.save(new FuncaoModel(2L, "Cliente"));
                }
                break;
            case "Tecnico":
                funcaoModel = funcaoRepository.findByNome(FuncaoModel.Values.TECNICO.getNome());
                if (funcaoModel == null) {
                    funcaoModel = funcaoRepository.save(new FuncaoModel(1L, "Técnico"));
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de usuário inválido: " + nomeFuncao);
        }
        return funcaoModel;
    }

    private UsuarioModel criarUsuario(String nome, String email, String senha, FuncaoModel funcaoModel) {
        UsuarioModel usuarioModel = new UsuarioModel();
        usuarioModel.setNome(nome);
        usuarioModel.setEmail(email);
        usuarioModel.setSenha(senha);
        usuarioModel.setFuncao(funcaoModel);

        return usuarioModel;
    }

    // Listar todos os chamados criados pelo usuário
    public List<ChamadoModel> listarTodosOsChamadosDoUsuario(ClienteModel usuario) {
        return chamadoRepository.findByCliente(usuario);
    }

    // Listar todos os chamados criados pelo usuário que ainda estão em aberto
    public List<ChamadoModel> listarChamadosEmAbertoDoUsuario(ClienteModel usuario) {
        return chamadoRepository.findByClienteAndStatus(usuario, ChamadoModel.TicketStatus.ABERTO);
    }

    // Mostra detalhes do chamado na pagina de usuário (não deve ter alterar o status na pagina)
    public Optional<ChamadoModel> obterDetalhesDoChamadoPorId(Long id) {
        return chamadoRepository.findById(id);
    }

}
