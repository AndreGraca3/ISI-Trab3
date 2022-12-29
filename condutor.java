
class condutor {
    private int idpessoa;
    private String nconducao;
    private LocalDate dtnascimento;

    condutor(String values){
        String[] attributos = values.split(","); // ()
        idpessoa = Integer.parseInt(attributos[0]);
        nconducao = attributos[1];
        dtnascimento = LocalDate.parse(attributos[2]);
    }

    public Integer getIdPessoa() { return idpessoa; }

    public String getNConducao() { return nconducao; }

    public LocalDate getDtNascimento() { return dtnascimento; }

    public void setIdPessoa(int idpessoa) { this.idpessoa = idpessoa; }

    public void setNConducao(String nconducao) { this.nconducao = nconducao; }

    public void setDtNascimento(LocalDate dtnascimento) { this.dtnascimento = dtnascimento; }

}