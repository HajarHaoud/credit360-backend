package ma.credit360.web.dto;

import java.util.List;

public class ChecklistDto {
    private int requises;
    private int deposees;
    private int manquantes;
    private boolean complete;
    private boolean modifiable;
    private List<PieceChecklistItemDto> pieces;

    public ChecklistDto() {}

    public ChecklistDto(int requises, int deposees, int manquantes, boolean complete, boolean modifiable, List<PieceChecklistItemDto> pieces) {
        this.requises = requises;
        this.deposees = deposees;
        this.manquantes = manquantes;
        this.complete = complete;
        this.modifiable = modifiable;
        this.pieces = pieces;
    }

    public int getRequises() { return requises; }
    public void setRequises(int requises) { this.requises = requises; }

    public int getDeposees() { return deposees; }
    public void setDeposees(int deposees) { this.deposees = deposees; }

    public int getManquantes() { return manquantes; }
    public void setManquantes(int manquantes) { this.manquantes = manquantes; }

    public boolean isComplete() { return complete; }
    public void setComplete(boolean complete) { this.complete = complete; }

    public boolean isModifiable() { return modifiable; }
    public void setModifiable(boolean modifiable) { this.modifiable = modifiable; }

    public List<PieceChecklistItemDto> getPieces() { return pieces; }
    public void setPieces(List<PieceChecklistItemDto> pieces) { this.pieces = pieces; }
}
