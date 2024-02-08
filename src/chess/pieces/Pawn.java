package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }


    // método paras os movimentos de peão do xadrez
    @Override
    public boolean[][] possibleMoves() {
        // matrix com as mesmas dimensões do tabulerio de xadrez, para que ele possa se mover para qualquer casa inicialmente
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColums()];
        Position p = new Position(0, 0);

        // peão branco vai se mover para cima na matrix de posições
        if (getColor() == Color.WHITE) {
            p.setVales(position.getRow() - 1, position.getColumn());
            // se a posição existir e estiver vazia
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // primeiro movimento pode ser duas casas,
            // mas a primeira casa, também tem de estar livre; não pode saltar peças
            p.setVales(position.getRow() - 2, position.getColumn());
            // posição criada para testar essa linha a frente do peão
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            // se a posição existir e estiver vazia
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // casas nas diagonais, onde ele pode realizar a captura
            // d1
            p.setVales(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // d2
            p.setVales(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // jogada especil en passant
                // peão tem que estar na linha 3 (matriz tabuleiro) seria linha 5 do xadrez
            if (position.getRow() == 3) {
                // verificar se tem uma peça adversária ao lado
                // esquerda
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                // se a peça à esquerda existe, é adversária e está vulnerável a tomar o en passant
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() - 1][left.getColumn()] = true;
                }
                // direita
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                // se a peça à direita existe, é adversária e está vulnerável a tomar o en passant
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() - 1][right.getColumn()] = true;
                }
            }

        } // peça preta agora
        else {
            // serão os mesmos movimentos da branca, só que somando a linha,
            // pois estará descendo no tabuleiro
            p.setVales(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            p.setVales(position.getRow() + 2, position.getColumn());
            // posição criada para testar essa linha a frente do peão
            Position p2 = new Position(position.getRow() + 1, position.getColumn());

            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // casas nas diagonais, onde ele pode realizar a captura
            // d1
            p.setVales(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            // d2
            p.setVales(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            // jogada especil en passant
            if (position.getRow() == 4) {
                // verificar se tem uma peça adversária ao lado
                // esquerda
                Position left = new Position(position.getRow(), position.getColumn() - 1);
                // se a peça à esquerda existe, é adversária e está vulnerável a tomar o en passant
                if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().piece(left) == chessMatch.getEnPassantVulnerable()) {
                    mat[left.getRow() + 1][left.getColumn()] = true;
                }
                // direita
                Position right = new Position(position.getRow(), position.getColumn() + 1);
                // se a peça à direita existe, é adversária e está vulnerável a tomar o en passant
                if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().piece(right) == chessMatch.getEnPassantVulnerable()) {
                    mat[right.getRow() + 1][right.getColumn()] = true;
                }
            }
        }
        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }
}
