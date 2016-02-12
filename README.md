# Life Tracker

Keep your life together.

## Table of Contents

 1. About
 2. Authors
 3. Quick Start
 4. Feature Details

## About

Have you ever wanted to keep track of your deadlines and scheduling your events in just a simple one-line command? How about editing, searching, and finding the deadlines on-the-go?

Life Tracker does all that and more! It is a simple tool that will help you keep track of all your upcoming deadlines and events.. On top of being able to add/edit/update/delete your deadlines and events,  you can see how long you have until your deadline. You can even save your schedule both online and  offline to be able to access it anywhere!

## Authors

 - Shen Yichen
 - Nithi Chindaraksawong
 - Muhammad Haikal Bin Rosman
 - Shang Shuqi


## Quick Start

 1. Press Ctrl + Spacebar to bring up Life Tracker.
 2. Try adding new deadline with a simple command. Here are some examples:
  - `Do homework by 7pm`
 4. View your deadlines.
 5. Try more commands.

To learn more details of Life Tracker features, refer to the 'Feature Details' section below.

## Feature details

### add

The add command lets you add a task, like:

```
add Water Plants by 6pm
```

Variants can allow you to add events, with a start and end time, or tasks with no due date/time. See the table below.

Note that as a command, `add` is optional. Typing a command without the first keyword will automatically be considered an `add`.
Thus,
```
Water Plants by 6pm
```
will work too.

> [option] is an optional option, and option1&#124;option2 means either option1 or option2 can be specified.

| Command | Description |
| ------- | ----------- |
|[add] &lt;task&gt; | Adds a floating task, with the name as specified in $lt;task&gt;|
| [add] &lt;task&gt; by [date] [time] | <p>Adds a deadline task, by the name specified.</p><p>If no date is given, it is set to the next date where [time] is still in the future.</p><p>If no time is given, it is set to 2359.</p><p>If nothing is entered after the “by” keyword, deadline defaults to the day itself at 2359.</p> |
| [add] &lt;event&gt; from [start_date] &lt;start_time&gt; to [end_date] [end_time] | <p>Add a task with specific beginning time and end time.</p><p>The default date when neither is provided is the next closest date where the end time is in the future.</p><p>The default end_date if start_date is provided is the closest date based on the end_time such that the event ends after it starts.</p><p>The default end_time is 1hr after the start time.</p> |
| [add] &lt;cmd&gt; every &lt;interval&gt; | <p>Make the event/task recurring.</p><p>&lt;cmd&gt; can be either a event or a deadline task command, as above.</p><p>The event/task will repeat itself every interval starting from the time stated in &lt;cmd&gt;.</p><p>Entering a floating task as &lt;cmd&gt; will cause the task to be transformed into a deadline task.</p> |

### list

List is the search command, and works by filters. E.g.:

```
list water after 12/01
```

lists all tasks/events with the word "water" and with dates associated that occur after 12th January this year.

| Command | Description |
| ------- | ----------- |
| list [search_string] [on&#124;after&#124;before [date][time]] | <p> List tasks that contains the text.</p><p>Returns everything if nothing is specified.</p><p>The on, after, before filters can each be specified once to filter for elements based on date and time.</p> |

### delete

Delete removes tasks/events through searching for terms in it's name. If many matching candidates are found, they will be listed so you can choose which to delete.

| Command | Description |
| ------- | ----------- |
| delete &lt;search_string&gt; | Finds and delete a task. The user is prompted with a list on multiple matches. |

### edit

Edit can be used to update tasks/events. It also works by matching tasks/events with the term provided. Then, you can enter the edit string, which is in the exact same syntax as `add`. E.g.
```
edit Water Plants > Water Garden on 12/1
```
will change the Water Plants tasks to be on 12th January.

| Command | Description |
| ------- | ----------- |
| edit &lt;search_string&gt; &gt; &lt;cmd&gt; | <p>Changes the tasks/event accordingly to &lt;cmd&gt;, which should take the syntax of any `add` command.</p><p>The defaults of `add` will be ignored if the tasks/event already have values in those fields. |

### Miscellaneous

| Command | Description |
| ------- | ----------- |
| undo | Undo the last operation. |
| mark &lt;search_string&gt; | Mark a task as done, same behaviour as delete. |
| savedir &lt;dir/file&gt; | Sets the file the program saves to. |
| exit | Exit Life Tracker. |

### Date and Time

Date is specified in DDMMYY or DDMMYYYY format, separators are optional. Year, or year and month can be omitted. If so they are replaced by the current month/year.

Time can be specified in 24-hr format with a 'h' suffix e.g. `2359h`. 12-hr time ends with either 'am' or 'pm', e.g. `1259pm`. Separators are optional, and minutes can be omitted, to be replaced with 00 by default.
