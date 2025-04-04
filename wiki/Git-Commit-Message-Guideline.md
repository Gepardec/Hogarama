# Why?

Without proper care, Git easily devolves into a memory dump, and even though all information is present, retrieving it
becomes a despairing walk through a bewildering maze with many dead-end branches and indecipherable signposts.

To prevent the commit review and code maintenance from becoming an outbreak of raging frustration, we want to encourage
our developers to adhere to some simple rules on how to write the commit messages and communicate their work.

# How!

First, organise your commits into logically self-contained (atomic) changesets. If you have worked on many issues, try
to split them up and commit the changes for each issue separately.

Have your collaborators in mind while you write: Your commit message should not be a mnemonic device for you to remember
what you did, but communicate to others (and yourself in two weeks) your work. It should tell what you did and why. The
changes themselves tell how it was done.

Git and IDEs offer great tooling to visualise the code changes and there is no need for redundant comments in the git
message. Tell what is not in the code:

1. **Motivation**: Why did you make the changes?
2. **Goal**: What will your changes achieve?

## Message Format

Consult [Tim Pope's template](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html) (
from [[http://git-scm.com/book/ch5-2.html]])

 ```
 Short (50 chars or less) summary of changes
 
 More detailed explanatory text, if necessary.  Wrap it to about 72
 characters or so.  In some contexts, the first line is treated as the
 subject of an email and the rest of the text as the body.  The blank
 line separating the summary from the body is critical (unless you omit
 the body entirely); tools like rebase can get confused if you run the
 two together.
 
 Further paragraphs come after blank lines.
 
   - Bullet points are okay, too
 
   - Typically a hyphen or asterisk is used for the bullet, preceded by a
     single space, with blank lines in between, but conventions vary here
 ```

Those tips above are not only a matter of style, but Git and other tools, like IDEs, often follow these guidelines for
display and their internal working.

## 7 Rules

according to [Chris Beams](https://chris.beams.io/posts/git-commit/)

1. Separate **subject** from **body** with a **blank line**
2. Limit the **subject** line to **50** characters
3. **C**apitalize The **Subject** Line
4. **DO NOT** end the **subject** line with a period
5. Use the **imperative** mood in the subject line
6. Wrap the **body** at **72** characters
7. Use the body to explain **what and why** vs. how (your code tells the how)

## Linking to Issue

If your commit closes a well-written issue, then you can simply write an one-liner like:

``` 
Fix #issue-no° Wrong calculation of water for roses
```

Never write only the issue number since nowbody knows all issue numbers by heart.
And assuming you adhere as closely to the issue writing guideline as to this how-to, your colleagues will be informed by
the issue itself about your work.

# Sources

- [Tim Pope](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
- [Git Documentation](https://git-scm.com/book/en/v2/Distributed-Git-Contributing-to-a-Project)
- [Chris Beams](https://chris.beams.io/posts/git-commit/)
