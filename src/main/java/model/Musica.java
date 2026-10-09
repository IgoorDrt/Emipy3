package model;

public class Musica {
    private int id;
    private String titulo;
    private int duracaoSeg;
    private String genero;
    private Artista artista;
    private Album album;

    public Musica() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public int getDuracaoSeg() { return duracaoSeg; }
    public void setDuracaoSeg(int duracaoSeg) { this.duracaoSeg = duracaoSeg; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Artista getArtista() { return artista; }
    public void setArtista(Artista artista) { this.artista = artista; }
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public String getDuracaoFormatada() {
        return String.format("%d:%02d", duracaoSeg / 60, duracaoSeg % 60);
    }

    @Override
    public String toString() { return titulo; }
}
