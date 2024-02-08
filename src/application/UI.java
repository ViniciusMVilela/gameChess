package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI {
    // importação de código para colorir o tabuleiro
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    //método para limpar a tela
    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // método que imprime o turno, currentPlayer e tabuleiro
    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        // imprimir as peças capturadas
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());
        // testar checkMate
        if (!chessMatch.getCheckMate()) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());

            if (chessMatch.getCheck()) {
                System.out.println("CHECK");
            }
        } else {
            System.out.println("CHECK MATE!");
            System.out.println("Winner: " + chessMatch.getCurrentPlayer());
        }
    }

    // recebe uma matrix de peças para conseguir imprimí-las
    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // sobrecarga do método, agora recebendo também a matriz de movimentos possíveis
    // para conseguir imprí-las no tabuleiro
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    //método para ler uma posição do usuário que o usuário vai fornecer para mover as peças, vai receber um Scanner
    // que será instanciado no programa principal
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine();
            // dado uma posição a1
            // coluna é o primeiro caractere da ChessPosition
            char column = s.charAt(0);
            // recortando o ‘String’ a partir da posição 1 (2° caractere) e convertendo para int
            int row = Integer.parseInt(s.substring(1));

            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            // erro de entrada de dados
            throw new InputMismatchException("Error readind ChessPosition. Valid values are from a1 to h8.");
        }
    }

    // método auxiliar para imprimir uma peça
    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) { //siginifica que não há peça na posição do tabuleiro
            System.out.print("-" + ANSI_RESET);
        } else { // caso não, imprimimos a peça
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
            }

        }
        System.out.print(" "); // espaço para que as peças não fiquem coladas
    }

    //método para imprimir as peças capturadas
    private static void printCapturedPieces(List<ChessPiece> captured) {
        // filtrando da lista de peças, as peças brancas
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());

        //lógica para imprimir as peças
        System.out.println("Captuted pieces: ");
        System.out.print("White: ");
        // adiciona cor branca
        System.out.print(ANSI_WHITE);
        // maneira de imprimir array de valores
        System.out.println(Arrays.toString(white.toArray()));
        // reseta cor da impressão
        System.out.print(ANSI_RESET);
        System.out.print("Black: ");
        // adiciona cor preta
        System.out.print(ANSI_YELLOW);
        // maneira de imprimir array de valores
        System.out.println(Arrays.toString(black.toArray()));
        // reseta cor da impressão
        System.out.print(ANSI_RESET);
    }
}