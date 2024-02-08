package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board; // uma partida precisa de um tabuleiro
    //valor default = false;
    private boolean check; // boleano que irá retornar true caso o rei estiver em check

    private boolean checkMate; // boleano que irá retornar true se o rei estiver em checkmatch

    private ChessPiece enPassantVulnerable; // default null

    private ChessPiece promoted;


    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8); // a partida que deve saber o tamanho do tabuleiro, então já deixamos instaciado aqui
        turn = 1; // início com o turno 1
        currentPlayer = Color.WHITE; // jogador de peçs brancas que inicia o jogo
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    //retorna uma matriz de peças correspondente a partida
    public ChessPiece[][] getPieces() {
        // a matrix de peças deve ter a mesma quantidade do tabuleiro
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColums()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColums(); j++) {
                // para cada peça do tabuleiro fazemos um downcasting para peça de xadrez
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    // método que vai nos auxiliar a printar os movimentos possivéis dado
    // posição de origem selecionada
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        //convertendo para uma posição de matriz
        Position position = sourcePosition.toPosition();
        // valida a posição
        validateSourcePosition(position);
        // retorna a matriz de movimentos possíveis
        return board.piece(position).possibleMoves();
    }


    // método responsável por ler os comandos de mover a peça, de uma posição atual para uma posição desejada
    // e capturar uma peça adversária caso necesário → ainda não implementada
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) throws IllegalAccessException {
        // convertando posições para posições de matriz
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        //validação da posição origem
        validateSourcePosition(source);
        //validação da posição destino
        validateTargetPosition(source, target);
        // operação que realiza o movimento da peça, já no formato de matriz
        Piece capturedPiece = makeMove(source, target);

        // se isso for verdade ele se colocou em check
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        // referência da peça que se moveu
        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // jogada especial promoção
        promoted = null; // assegurar que este é um novo teste
        if (movedPiece instanceof Pawn) {
            if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                // o peão foi a peça promovida
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q"); //troca padrão por queen
            }
        }

        // verificar se o oponente ficou em check
        check = (testCheck(opponet(currentPlayer))) ? true : false;

        // testar se a jogada gerou checkMate, para encerrar o jogo
        if (testCheckMate(opponet(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        // tratamento do en Passant
        // duas condicionais para fazer incluir as duas cores de peões
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2) || target.getRow() == source.getRow() + 2) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        // fazendo donwcasting para ChessPiece, porque a peça capturada era do tipo Piece
        return (ChessPiece) capturedPiece;
    }

    // método que realiza a promoção da peça pawn
    public ChessPiece replacePromotedPiece(String type) throws IllegalAccessException {
        if (promoted == null) {
            throw new IllegalAccessException("There is no piece to be promoted");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
           return promoted;
        }

        Position pos = promoted.getChessPosition().toPosition();
        // removemos a peça e alocamos ela em p
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        // instaciar nova peça conforme a seleção do usuário e colocá-la no lugar de pawn
        ChessPiece newPiece = newPiece(type, promoted.getColor());
        // inserir na posição de pawn; pos
        board.placePiece(newPiece, pos);
        // adicionar a matriz de peças do tabuleiro
        piecesOnTheBoard.add(newPiece);

        return  newPiece;
    }

    // método auxiliar para instaciar a nova peça na hora da promoção
    private ChessPiece newPiece(String type, Color color) {
        if(type.equals("B")) return new Bishop(board, color);
        if(type.equals("N")) return new Knight(board, color);
        if(type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);

    }

    private Piece makeMove(Position source, Position target) {
        // removemos a peça na posição de origem
        ChessPiece p = (ChessPiece) board.removePiece(source);
        // anotando o movimento de peça
        p.increaseMoveCount();
        // capturamos e removemos uma possível peça na posição destino
        Piece capturedPiece = board.removePiece(target);
        // inserimos a peça na posição destino
        board.placePiece(p, target);

        // se no movimento uma peça foi capturada
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // Roque pequeno
        // identificando a jogada, e movendo a torrre manualmente
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            // posição origem da torre
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            // posição destino da torre
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            // agora movemos a torre
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // Roque grande
        // identificando a jogada, e movendo a torrre manualmente
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            // posição origem da torre
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            // posição destino da torre
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            // agora movemos a torre
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // en passant
        if (p instanceof Pawn) {
            // se o peão moveu em diagonal e não houve captura; parou em cima de uma peça adversária
            // ele realizou o en passant
            // coluna diferente = movimento diferente de ir para cima ou para baixo
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                // realizamos a captura de peça nessa posição
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        return capturedPiece;
    }

    //método para desfazer um movimento
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        // iremos fazer a lógica contrária de makeMove
        ChessPiece p = (ChessPiece) board.removePiece(target);
        // decrementando a contagem de movimento
        p.decreaseMoveCount();
        board.placePiece(p, source);

        // recolocando uma possível peça capturada no tabuleiro
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }

        // desfazendo jogada especial Roque
        // Roque pequeno
        // identificando a jogada, e movendo a torrre manualmente
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            // posição origem da torre
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            // posição destino da torre
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);

            // agora movemos a torre
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // Roque grande
        // identificando a jogada, e movendo a torrre manualmente
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            // posição origem da torre
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            // posição destino da torre
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);

            // agora movemos a torre
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }

    }

    //recebe uma posição e valida se existe peça na mesma
    public void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        // downcasting para chessPiece porque getColor é um get dele e não de piece
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    // recebe as posições e valida se o movimento é possível conforme os movimentos possíveis da peça
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position.");
        }
    }

    // método que incrementa o turno e altera o jogador atual
    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // método que retorna quem é oponente
    private Color opponet(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }


    // método que localiza um rei dada uma cor
    private ChessPiece king(Color color) throws IllegalAccessException {
        // filtragem da lista pela cor passada
        // piece não possui cor, mas ChessPiece, sim, então fazemos um downcasting
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

        for (Piece p : list) {
            // se para for uma instância de rei, encontramos a peça rei
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalAccessException("There is no " + color + "king on the board");
    }

    // método que indica se o rei está em check, varrendo todos os possíveis movimentos
    // das peças adversárias
    private boolean testCheck(Color color) throws IllegalAccessException {
        // capturamos a posiçao do rei e convertemos para tabuleiro,
        // pois os movimentos estão armazenados dessa forma
        Position kingPosition = king(color).getChessPosition().toPosition();
        // filtramos as peças adversárias
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == opponet(color)).collect(Collectors.toList());

        // verificamos para cada se esta possui um movimento possível para a posição do rei
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    // método para verificar um possível checkMate
    private boolean testCheckMate(Color color) throws IllegalAccessException {
        // se não estiver em check, não está em checkMate
        if (!testCheck(color)) {
            return false;
        }
        // testar se todas as peças possuem um movimento para reverter a situação
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color).collect(Collectors.toList());

        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColums(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);

                        // realizo o movimento possível para essa peça encontrada
                        Piece capturedPiece = makeMove(source, target);
                        // testo se com esse movimento o rei ainda continua em check
                        boolean testCheck = testCheck(color);
                        // desfazemos o movimento
                        undoMove(source, target, capturedPiece);

                        // significa que o movimento saiu do check
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    // método que recebe as cordenadas do xadrez e passa para matrix com o toPosition
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        // adicionando a peça criada na lista de peças do tabuleiro
        piecesOnTheBoard.add(piece);
    }

    // método responsável por iniciar a partida, colocando as peças no tabuleiro
    public void initialSetup() {
        /* assim definiamos antes do ChessPosition e placeNewPiece
        // piece and position
        // com cada construtor sendo passado nos parênteses
        // essas posições ainda correspondem com as posições da matriz e não do tabuleiro → depois será implementado

        board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
        board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
         */

        //agora chamamos o placeNewPiece, passamos posições do xadrez e ele já converte
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
