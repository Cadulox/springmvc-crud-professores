package br.com.cadulox.regescweb.dtos;

import br.com.cadulox.regescweb.models.Professor;
import br.com.cadulox.regescweb.models.StatusProfessor;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class RequisicaoFormProfessor {
    // É uma classe DTO (Data Transfer Object)

    @NotBlank
    @NotNull
    private String nome;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal salario;
    private StatusProfessor statusProfessor;

    public Professor toProfessor() {
        var professor = new Professor();
        professor.setNome(this.nome);
        professor.setSalario(this.salario);
        professor.setStatusProfessor(this.statusProfessor);

        return professor;
    }

    public Professor toProfessor(Professor professor) {
        professor.setNome(this.nome);
        professor.setSalario(this.salario);
        professor.setStatusProfessor(this.statusProfessor);
        return professor;
    }

    public void fromProfessor(Professor professor) {
        this.nome = professor.getNome();
        this.salario = professor.getSalario();
        this.statusProfessor = professor.getStatusProfessor();
    }
}
