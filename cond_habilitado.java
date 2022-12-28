
class cond_habilitado{
    private int condutor;
    private int veiculo;

    cond_habilitado(String values){
        String[] attributos = values.split(",") // (1,3)
        condutor = Integer.parseInt(attributos[0]);
        veiculo = Integer.parseInt(attributos[1]);
    }

    public Integer getCondutor() { return condutor; }

    public Integer getVeiculo() { return veiculo; }

    public void setCondutor(int id_condutor) { this.condutor = id_condutor; }

    public void setVeiculo(int id_veiculo) { this.veiculo = id_veiculo; }
}