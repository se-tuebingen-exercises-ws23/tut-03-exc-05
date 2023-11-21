# Exercise #05 - Best Practices & Documentation

## OPTIONAL: Tooling

<details>
<summary>If your tutorial hasn't done that yet, click here to expand</summary>

> A skilled craftsman must know how to use their tools in order to perform their craft well.

**Goal:** Can use the IDE efficiently.

1. Open this repository in the IDE of your choice
2. Hover over a type and observe the tooltip, hover over a term and observe the tooltip
3. Use 'go to definition' and look at the definition of a name
4. Use 'find usages', use 'find implementation' on a method/class/trait to find where it's used
5. Use right click to see what other actions the IDE can perform
6. Use context actions (the little lightbulb 💡)

</details>

## Documentation

> Documentation is a love letter that you write to your future self.

**Goal**: Know how to view and write ScalaDoc internal documentation.

0. Read the external documentation of Hexacraft below.
    - What is its audience? What is it describing?
    - Is there something missing from the external documentation? (Hint: Do you know where to go if you want to change something?)
1. Open this repository in the IDE of your choice
2. Go to https://scala-lang.org/api/3.x/ and find the documentation online
    - https://scala-lang.org/api/3.x/scala/collection/immutable/List.html
3. Look at the source for that documentation
    - https://github.com/scala/scala/blob/v2.13.8/src/library/scala/collection/immutable/List.scala
    - Figure out how the syntax works and how it relates to what the IDE helps you out with
4. Write a very small documentation comment (in ScalaDoc format) on an undocumented definition in this repository
5. Observe that it is now shown in the IDE when you hover over an occurence of the name
6. Use `sbt doc` in this repository to generate documentation for this project
7. Open `./target/scala-3.3.1/api/index.html` in your favorite browser and try to find your comment :)

## Style guides

> And guess what? That's never going to happen now. Because there's no way I'm going to be with someone who uses spaces over tabs.

**Goal**: Set-up your own coding style, enforce it automatically.

1. Look at the Scala style guide
    - Start with something familiar like the 'Indentation' section
    - https://docs.scala-lang.org/style/indentation.html 
2. Discuss the Scala style guide
    - What do you like? Why? 
    - What don't you like? Why?
    - How would you change it? What impact would it have?
    - Why do we need style guides at all?
    - Do you know any style guides for other languages?
        - How are those style guides different to this one?
        - Can there be multiple style guides for one language? Why?
3. Open this repository in the IDE of your choice
4. Mess up the formatting and then use `scalafmt` to fix it
    - `sbt scalafmtCheck` just checks without changing formatting
    - `sbt hexacraft/scalafmt` just formats the base project
        - Explanation: `sbt` uses `/` for scoping -- this runs the `scalafmt` for the `hexacraft` project
        - This is useful if there are many projects under the same `sbt` build file.
    - Run `git diff` to see the changes
    - Use `git checkout -p` to revert the changes
5. The tutor will now enable `scalafmt` on this repository:
    - in CI, so that all new changes are checked for style 😎
        - pull requests will be checked for style
6. Enable scalafmt in your IDE
    - see https://scalameta.org/scalafmt/docs/installation.html, sections IntelliJ and Metals
7. Configure scalafmt
    - Based on the previous discussion, look at the scalafmt options about what can be configured:
        - https://scalameta.org/scalafmt/docs/configuration.html
    - Where do you configure this in the project?

## RE: Naming

> [!Note]
> We use the phrase _identifier_ to denote a name of a variable/class/field/function/trait/...

1. Look at the slides and the Scala style guide to find out what makes a good _identifier_
2. Find an identifier which can be improved.
3. Rename the identifier, make sure not to break anything
    - Be prepared to justify why your changes improve the codebase
    - Please use only English names (if at all possible)

---

# Hexacraft

A game with hexagonal blocks on a cylindrical world inspired by Minecraft

Official website: https://martomate.com/games/hexacraft/

![image](https://martomate.com/games/hexacraft/hexacraft_in_game_0.10.png)

## Features:

- Hexagonal blocks
- Cylindrical world
- Infinite world (except around the cylinder)
- Triangular pixels
- And much more!

## Installation

First, download and install `sbt`.

In order to:
- _compile_ the project, run `sbt compile` in your console.
- _run_ the project, run `sbt run` in your console.

## Usage

### Controls
The controls are almost the same as in Minecraft.

#### Movement:
Action          | Keys
--------------- | -------------------------------
Movement        | WASD
Camera rotation | Mouse or arrow keys, PgUp, PgDn
Jump            | Space
Fly             | F
Fly up          | Space
Fly down        | Left Shift
Walk slow       | Left Ctrl
Walk fast       | Left Alt
Walk superfast  | Right Ctrl

#### Other useful controls
Action             | Keys
------------------ | ---------------------
Select hotbar slot | 1 ... 9, scroll wheel
Place block        | Right click
Remove block       | Left click
Pause              | Escape
Fullscreen         | F11

#### Extra
Action                      | Keys
--------------------------- | ---------------------
Free the mouse              | M
Debug info                  | F7
Place block under your feet | B

### Worlds are saved

The program works very similarly to Minecraft (at least when it comes to saving worlds).

You can create as many worlds as you like and they will be saved on your computer. To find the files go to the folder named ".hexacraft" in the %appdata% directory on Windows and in the home directory on Mac and Linux (just like for Minecraft).

## Credits

The game was created in spring of 2015 by **Martin Jakobsson** ([Martomate](https://github.com/Martomate)).

> **Warning** This repository is based on a project outside of our university.
> Please be mindful of the author's wishes. Although some changes might be desired and accepted here
> for educational purposes, it doesn't automatically mean that the original author wants them.

The original repository is located [here](https://github.com/Martomate/Hexacraft).

## Licence

This project goes under the [MIT Licence](LICENSE). In short it says that anyone may use this project for anything they want as long as the licence is not removed or changed.

Basically this should be an open source project, but if anyone uses it for some purpose it would be nice if they mentioned the original project or the authors of this project. There has, after all, been a lot of work put into this.
