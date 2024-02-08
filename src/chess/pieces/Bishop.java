package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {
    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColums()];

        // variável auxiliar, vai receber os valores das casas do tabuleiro
        // nas respectivas direções e verificar se o movimento é possível ou não
        Position p = new Position(0, 0);

        // NW (noroeste)
        // posição na linha e coluna - 1
        p.setVales(position.getRow() - 1, position.getColumn() - 1);
        // enquanto a posição p existir e não houver peça lá, é movimento B possível
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha e coluna para continuar a verificação
            p.setVales(p.getRow() - 1, p.getColumn() - 1);
        }
        // agora testamos se essa casa ocupada é uma peça adversária
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //NE (nordeste)
        // posição na linha - 1, coluna + 1
        p.setVales(position.getRow() - 1, position.getColumn() + 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setVales(p.getRow() - 1, p.getColumn() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //SE (sudeste)
        // posição na linha + 1, coluna + 1
        p.setVales(position.getRow() + 1, position.getColumn() + 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setVales(p.getRow() + 1, p.getColumn() + 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        //SW (sudoeste)
        // posição na linha + 1, coluna - 1
        p.setVales(position.getRow() + 1, position.getColumn() - 1);

        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;

            // decremantamos mais uma linha para continuar a verificação
            p.setVales(p.getRow() + 1, p.getColumn() - 1);
        }

        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }


    @Override
    public String toString() {
        return "B";
    }
}
