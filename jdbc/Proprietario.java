package jdbc;

import java.time.LocalDate;

import javax.xml.crypto.Data;

class Proprietario {
    private int idpessoa;
    private LocalDate dtnascimento;

    Proprietario(String values){
        String[] attributos = values.split(","); // ()
        idpessoa = Integer.parseInt(attributos[0]);
        dtnascimento = LocalDate.parse(attributos[1]);
    }

    public Integer getIdPessoa(){return idpessoa;}
    public LocalDate getDtNascimento(){return dtnascimento;}

    public void setIdPessoa(int idpessoa) { this.idpessoa = idpessoa; }
    public void setDtNascimento(LocalDate dtnascimento) { this.dtnascimento = dtnascimento; }
}