# Jogo de Xadrez 
Jogo de xadrez desenvolvido em Java 
### Como jogar 
Para jogar, devemos run application/Program
O tabuleiro será carregado como o exemplo abaixo
```
8 R N B Q K B N R 
7 P P P P P P P P 
6 - - - - - - - - 
5 - - - - - - - - 
4 - - - - - - - - 
3 - - - - - - - - 
2 P P P P P P P P 
1 R N B Q K B N R 
  a b c d e f g h

Captuted pieces: 
White: []
Black: []

Turn: 1
Waiting player: WHITE
```
#### Movimentos
As peças são movimentadas pelas posições do tabuleiro, primeiro passamos a posição "Source" para indicar em que posição está a peça que queremos mover
os movimentos possíveis para essa peça serão marcados no tabuleiro, e passamos a posição "Target" para indicar a posição de destino da peça
