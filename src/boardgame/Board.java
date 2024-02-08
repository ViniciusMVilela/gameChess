package boardgame;

public class Board {
    private int rows;
    private int colums;
    private Piece[][] pieces;

    public Board(int rows, int colums) {
        if (rows < 1 || colums < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }

        this.rows = rows;
        this.colums = colums;
        pieces = new Piece[rows][colums]; // a matriz de peças será gerada com a qntd linhas e colunas informadas.
    }

    public int getRows() {
        return rows;
    }


    public int getColums() {
        return colums;
    }


    // retornando uma peça da matrix pieces dado uma linha e coluna
    public Piece piece(int row, int column) {
        if(!positionExists(row, column)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[row][column];
    }

    //sobrecarga do método
    //agora retornando a peça pela posição
    public Piece piece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }

        return pieces[position.getRow()][position.getColumn()];
    }

    // inserir na matriz a peça na posição passadas por argumento
    public void placePiece(Piece piece, Position position) {
        if(thereIsAPiece(position)) {
            throw new BoardException("There is already a piece on position " + position);
        }

        // atribuir a esta posição linha e coluna na matriz a piece
        pieces[position.getRow()][position.getColumn()] = piece;
        // atualizamos a posição de piece que antes era nula para a nova posição informada
        piece.position = position;
    }

    // método para remover uma peça de uma dada posição
    public Piece removePiece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        if(piece(position) == null) { // não há peça nessa posição
            return null;
        }
        Piece aux = piece(position);
        aux.position = null; //retiramos ela da posição
        // acessamos a matriz de peças e retiramos a peça do tabuleiro
        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }

    //método auxiliar de positionExists
    private boolean positionExists(int row, int column) {
        // para existir tem que conter no tabuleiro
        return row >= 0 && row < rows && column >= 0 && column < colums;
    }

    // recebe uma posição é retorna se a mesma existe ou não no tabuleiro
    public boolean positionExists(Position position) {
        return positionExists(position.getRow(), position.getColumn());
    }

    //verificar se exsite uma piece na posição passada
    public boolean thereIsAPiece(Position position) {
        if(!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }

        // usamos o método piece passando a posição
        // quando criamos a peça, ela possui uma posição atualizada pelo
        // placePiece e deixa de ser null
        return piece(position) != null;
    }
}
