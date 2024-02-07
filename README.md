# TCSS305-Group-Project

TCSS 305 Tetris

## Autumn 2023, Group 2

This is a java application of the retro Tetris game, made by:
- Johan Hernandez *(johannn@uw.edu)*
- Duy-Hung Cong Le *(hle4831@gmail.com)*
- Jayden Kitiona Peneueta *(Jkpeneueta117@gmail.com)*
- Ian Salsich *(ian4786@gmail.com)*

## Sprint1 (11/20/2023 - 11/26/2023)

### Contributions
Johan Hernandez: Tasked on the implemention of the TetrisPanel class.

Duy-Hung Cong Le: Tasked on the implemention of the NextPiecePanel class.

Jayden Kitiona: Tasked on the implemention of the InfoPanel class.

Ian Salsich: Tasked on the implemention of the BoardPanel class.

### Comments
...

## Sprint2 (11/27/2023 - 12/4/2023)

Required Log:

Sprint2 Labor Log: https://docs.google.com/document/d/1juadSmu303wJ0sxU1Bq7gOtnuVamfv6oIPOGNhDKulc/edit?usp=sharing

General Log:

https://docs.google.com/spreadsheets/d/1kYsk8IFE0w5jO2PVPRzud2J3UW9OPSC7sk8J_AIVSjw/edit?usp=sharing

### Contributions
Johan Hernandez: Tasked on the implemention of the Tetrisboard property change, and assisted in implementing the listeners for all panels.

Duy-Hung Cong Le: Tasked on the implemention the listeners and changes of the NextPiecePanel class.

Jayden Kitiona: Tasked on the implementation of the keylisteners for the tetris panel for movement within the tetris game

Ian Salsich: Tasked on implementing the board's pieces listeners as well as displays for the pieces.

### Comments

How do we fix our unchecked cast syntax error on BoardPanel class line 159?
We also have a Law of Demeter warning in the BoardPanel class.

## Sprint3 (12/5/2023 - 12/10/2023)


Sprint3 Labor Log: https://docs.google.com/document/d/1n2BigD3PydhulYiZQdineINXoDHoP6FkLGSa48x6BDc/edit?usp=sharing


General Labor Log: https://docs.google.com/spreadsheets/d/1kYsk8IFE0w5jO2PVPRzud2J3UW9OPSC7sk8J_AIVSjw/edit?usp=sharing

### Contributions
Johan Hernandez: Helped with cleaning up code syntax errors and help implement the infopanel requirments.

Duy-Hung Cong Le: Helped with cleaning up code and with fixing the infopanel visual.

Jayden Kitiona: Finding and fixing syntax errors.

Ian Salsich: Fixing syntax errors, as well as hosting group codings.

### Comments

Syntax Issues: 

InfoPanel line 171 'PrintStream' used without 'try'-with-resources statement : This is a new syntax error that we encountered and couldnt understand what the action to solve it would be.

BoardPanel line 278 Call to 'getBlock()' violates Law of Demeter : Law of Demeter violation due to method linking to get the block type so that we can determine what color it would be. Couldn't find a efficient way to fix it without making the process more complex.

BoardPanel line 278 Unchecked cast: 'java.lang.Object' to 'java.util.List<model.Block[]>' This checkstyle error is strange it says unchecked cast, but we do have the proper cast and the error didn't give us any hints or solutions to fix it.

### Extra Features

Extra Feature: Highscore/score saver.

Extra Credit Implementation: Grid toggle, sound effects, background music, highscore, GUI customization, and visual enhancements.

Scoring Algorithm: InfoPanel.Java, updateLevel method.





