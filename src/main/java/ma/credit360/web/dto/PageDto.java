package ma.credit360.web.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public class PageDto<T> {
    private List<T> contenu;
    private int page;
    private int taille;
    private long totalElements;
    private int nbPages;

    public PageDto() {}

    public PageDto(List<T> contenu, int page, int taille, long totalElements, int nbPages) {
        this.contenu = contenu;
        this.page = page;
        this.taille = taille;
        this.totalElements = totalElements;
        this.nbPages = nbPages;
    }

    public static <T> PageDto<T> from(Page<?> pageData, List<T> customContent) {
        return new PageDto<>(
                customContent,
                pageData.getNumber() + 1,
                pageData.getSize(),
                pageData.getTotalElements(),
                pageData.getTotalPages()
        );
    }

    public List<T> getContenu() { return contenu; }
    public void setContenu(List<T> contenu) { this.contenu = contenu; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getTaille() { return taille; }
    public void setTaille(int taille) { this.taille = taille; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

    public int getNbPages() { return nbPages; }
    public void setNbPages(int nbPages) { this.nbPages = nbPages; }
}
