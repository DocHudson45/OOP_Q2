# OOP QUIZ 2 REPORT - PREDICTIVE TEXT SYSTEM

Group Members:
| No. | Student ID | Student Name |
| :-- | :--- | :--- |
| 1 | 5025241010 | Muhammad Dzaky Radhitya Ryrdi |
| 2 | 5025241004 | Razan Widya Reswara |
| 3 | 5025241168 | Naufal Bintang Brillian |

1. Introduction In this project, we developed a T9 Predictive Text system similar to the technology used in older mobile phones. The main goal is to convert numeric keystrokes (e.g., "4663") into potential words (e.g., "good", "home"). We implemented three different approaches (List, Map, and Tree) to compare their performance and built a GUI with a modern interface.

2. Implementation & Questions

Part 1: Prototypes and Design We created the PredictivePrototype class to handle the basic logic.

Why use StringBuffer? (Q1.1) We used StringBuffer in the wordToSignature method because String objects in Java are immutable (cannot be changed). If we used the + operator in a loop, Java would create a new String object in memory for every character, which is very inefficient. StringBuffer is mutable, so it saves memory and time.

Why is the prototype inefficient? (Q1.2) The signatureToWords method in this part is inefficient because it does not store the dictionary in memory (RAM). For every single search, the program has to open the words file, read it line-by-line, and close it again. Reading from the disk is much slower than reading from memory.

Part 2: Storing and Searching (List) To solve the efficiency problem, we implemented DictionaryListImpl.

We read the dictionary file only once when the program starts and store it in an ArrayList.

We sorted the list and used Collections.binarySearch. This is much faster (O(log n)) than checking words one by one from the start.

Part 3: Efficiency and Prefix Matching We implemented two advanced data structures:

Map (DictionaryMapImpl): We used a HashMap. The key is the signature, and the value is a Set of words. This is the fastest method because it finds the word instantly (O(1)).

Tree (DictionaryTreeImpl): We built a Trie (Prefix Tree). Each node represents a number (2-9). This allows "Prefix Matching," meaning if the user types "46", the system can suggest "home" or "good" even though the user hasn't finished typing yet.

Pseudocode for Tree Insertion:
```
function insert(signature, word):
    if signature is empty:
        add 'word' to the current node
        return
    
    index = first char of signature - '2'
    
    if branches[index] is null:
        create new node at branches[index]
    
    call insert recursively on branches[index] with remaining signature
```
Part 4: GUI (Model-View-Controller) We built the interface using Java Swing following the MVC pattern.

Model: The Dictionary interface (swappable between Tree or List).

View: The PredictiveGUI class.

Controller: Logic to handle button clicks.

Design: We improved the UI to look like a modern "Glassmorphism" calculator. We used Graphics2D to draw custom transparent buttons with rounded corners and shadows instead of standard Java buttons.

3. Testing and Results

We measured the speed of each implementation using the command line time tool. Input used: 4663 43556 96753 69 6263 47

Time Comparison:

Prototype: ~1.20 seconds (Slowest, due to repeated file reading).

List Implementation: ~0.45 seconds (Fast).

Map/Tree Implementation: ~0.1 - 0.2 seconds (Fastest).


GUI Output: The GUI works correctly. The * button cycles through candidates, 1 acts as delete, and # accepts the word. The visual design is transparent as planned.

4. Work Distribution & Contribution

We divided the work equally (33.33% each) based on our roles:
| Student Name | Contribution | Role & Description of Contribution |
| :--- | :---: | :--- |
| **Muhammad Dzaky Radhitya Ryrdi** | **33.33%** | **Core Logic & Foundation.**<br>Responsible for Part 1 (`PredictivePrototype`) and Part 2 (`DictionaryListImpl`). Handled the initial file I/O, string-to-signature conversion logic, and implemented the Binary Search algorithm. |
| **Razan Widya Reswara** | **33.33%** | **Advanced Algorithms & Optimization.**<br>Responsible for Part 3 (`DictionaryTreeImpl` & `MapImpl`) including the recursive logic for Prefix Tree. Handled **Testing & Algorithm Finishing**, including performance benchmarking and logic refinement. |
| **Naufal Bintang Brillian** | **33.33%** | **Integration, UI Design & Reporting.**<br>Responsible for Part 4 (`PredictiveGUI`). Designed the custom **"Glassmorphism" UI** components using Java 2D graphics. Compiled the pseudocode analysis and the final report. |
