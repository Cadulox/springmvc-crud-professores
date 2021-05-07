package br.com.cadulox.regescweb.controllers;

import br.com.cadulox.regescweb.dtos.RequisicaoFormProfessor;
import br.com.cadulox.regescweb.models.Professor;
import br.com.cadulox.regescweb.models.StatusProfessor;
import br.com.cadulox.regescweb.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping
    public ModelAndView index() {

        List<Professor> professores = this.professorRepository.findAll();

        var mv = new ModelAndView("professores/index");
        mv.addObject("professores", professores);

        return mv;
    }

    @GetMapping("/novo")
    public ModelAndView novo(RequisicaoFormProfessor requisicao) {
        var mv = new ModelAndView("professores/novo");
        mv.addObject("listaStatusProfessor", StatusProfessor.values());

        return mv;
    }

    @PostMapping
    public ModelAndView create(@Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("\n******************* TEM ERROS ***************************\n");

            var mv = new ModelAndView("professores/novo");
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }
        var professor = requisicao.toProfessor();
        this.professorRepository.save(professor);

        return new ModelAndView("redirect:/professores/" + professor.getId());
    }

    @GetMapping("/{id}")
    public ModelAndView show(@PathVariable Long id) {
        Optional<Professor> optional = this.professorRepository.findById(id);

        if (optional.isPresent()) {
            var professor = optional.get();

            var mv = new ModelAndView("professores/show");
            mv.addObject("professor", professor);

            return mv;
        }

        return this.retornaErroProfessor("SHOW ERROR: Professor #" + id + " não encontrado!");
    }

    @GetMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Long id, RequisicaoFormProfessor requisicao) {
        Optional<Professor> optional = this.professorRepository.findById(id);

        if (optional.isPresent()) {
            var professor = optional.get();
            requisicao.fromProfessor(professor);

            var mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", professor.getId());
            mv.addObject("listaStatusProfessor", StatusProfessor.values());

            return mv;
        }

        return this.retornaErroProfessor("EDIT ERROR: Professor #" + id + " não encontrado!");
    }

    @PostMapping("/{id}")
    public ModelAndView update(@PathVariable Long id, @Valid RequisicaoFormProfessor requisicao, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var mv = new ModelAndView("professores/edit");
            mv.addObject("professorId", id);
            mv.addObject("listaStatusProfessor", StatusProfessor.values());
            return mv;
        }
        Optional<Professor> optional = this.professorRepository.findById(id);

        if (optional.isPresent()) {
            var professor = requisicao.toProfessor(optional.get());
            this.professorRepository.save(professor);

            return new ModelAndView("redirect:/professores/" + professor.getId());
        }

        return this.retornaErroProfessor("UPDATE ERROR: Professor #" + id + " não encontrado!");
    }

    @GetMapping("{id}/delete")
    public ModelAndView delete(@PathVariable Long id) {
        var mv = new ModelAndView("redirect:/professores");

        try {
            this.professorRepository.deleteById(id);
            mv.addObject("mensagem", "Professor #" + id + " deletado com sucesso!");
            mv.addObject("erro", false);
        } catch (EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
            mv = this.retornaErroProfessor("DELETE ERROR: Professor #" + id + " não encontrado!");
        }

        return mv;
    }

    private ModelAndView retornaErroProfessor(String msg) {
        var mv = new ModelAndView("redirect:/professores");
        mv.addObject("mensagem", msg);
        mv.addObject("erro", true);
        return mv;
    }
}
