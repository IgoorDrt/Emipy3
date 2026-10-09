package model;

public class Album {
    private int id;
    private String titulo;
    private int ano;
    private Artista artista;

    public Album() {}
    public Album(int id, String titulo, int ano, Artista artista) {
        this.id = id; this.titulo = titulo; this.ano = ano; this.artista = artista;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }

    @Override
    public String toString() { return titulo; }
}
