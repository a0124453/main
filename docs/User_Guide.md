# User Guide

## Table of Contents

 1. Quick Start
 2. Feature Details


## Quick Start

### Setup

 1. Download and install Java 8 (JRE is fine), from [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
 2. Download the .jar file from our [releases page](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
 3. Place the .jar file at a convenient location and double-click it to run!

### Usage

 1. Try adding some simple entries, like:
    - `Submit report by 7pm`
    - `Movie from 2pm to 3pm tmr`
    - `Team meeting from wed 2pm to 3pm every week`
    - The command `add` can be used too for special cases, but is optional otherwise
     - `add add header to report`
 2. If you make a mistake, `undo` and `redo` will set things right
    - * You can also scroll through what you typed with the up/down arrow keys
 3. When your done with something, simply mark it based on it's ID to archive it. Recurring entries like "Team meeting" will be advanced automatically.  
    - `mark 3`
 4. You can search for entries with `find`
    - `find meeting`
    - `findall` and `findold` can search for archived entries.
 5. To change a task, use `edit` with an ID and the new values:
    - `edit 1 > by 6pm`
    - To change one occurrence for a recurring entry, use `editone` instead.
 6. To permanently delete something:
    - `delete 2`

> To learn more details of Life Tracker features, refer to the [Feature Details](#feature) section below.

## <a name="feature">Feature Details</a>

### Format

 - Square brackets: `[...]` indicates an optional command/field.
 - Pointed brackets: `<..>` indicated a required field.
 - Horizontal bar: `opt1 | opt2` means either opt1 or opt2 can be specified.

### Adding entries

The common form of all add command entry is:

```
[add]<task>[KeywordPhrase]
```

where the list of KeywordPhrase can be found below.
> Although the word `add` is optional, if you required the word `add` to be part of your task name, do type `add add...` to identify it as part of the name. This applies to other command keywords such as `edit`, `list`, etc. as well.

| KewordPhrase | Description |
| ------------ | ----------- |
| NULL | When no phrase is given, a floating task will be created with no deadlines |
| by [date and time] | <p>Add a task with a deadline specified by `date and time` field.</p><p>If the `date and time` field is empty after the `by` keyword, the deadline defaults to the day itself at 2359.</p> |
| at | Alternative keyword to "by". Works the same way but cannot be used concurrently. |
| from [date and time] | <p>Add an event with a start date specified by `date and time` field.</p><p>If the `date and time` field is empty after the `from` keyword, the start date defaults to the day itself at nearest hour to the current time.</p><p>MUST NOT be used concurrently with `by` or `at` keyword.</p> |
| to [date and time] | <p>Add an event with an end date specified by `date and time` field.</p><p>If the `date and time` field is empty after the `to` keyword and the start date is not specified, the end date defaults to the day itself at nearest hour to the current time. Otherwise, the end date defaults to one hour after the start date</p><p>Should be used concurrently with `from` keyword. MUST NOT be used concurrently with `by` or `at` keyword.</p>
| every [period] | <p>Make the event/task recurring with the interval specified by `period` field.</p><p>Can be used with any other keywords above.</p> |
| until [date] | <p>Set the date limit for recurring event/task as specified by `date` field.</p><p>MUST only be used with `every` keyword.</p> |
| for [no. of times] | <p>Set the no. of times for recurring event/task to be repeated.</p><p>MUST only be used with `every` keyword. MUST NOT be used concurrently with `until` keyword.</p> |
> The order of the keywords does not matter e.g. `dinner from 7pm to 9pm` works the same way as `dinner to 9pm from 7pm`.<br>You can leave parts of `date and time` field empty; LifeTracker will find the appropriates values automatically.<br>

| Command | Description |
| ------- | ----------- |
|[add] &lt;task&gt; | Adds a task without deadlines, with the name as specified in &lt;task&gt;|
| [add] &lt;task&gt; by&#124;at [date] [time] | <p>Adds a task with deadline, by the name specified.</p><p>If no date is given, it is set to the next date where [time] is still in the future.</p><p>If no time is given, it is set to 2359.</p><p>If nothing is entered after the “by” keyword, deadline defaults to the day itself at 2359.</p> |
| [add] &lt;event&gt; from [start_date] &lt;start_time&gt; to [end_date] [end_time] | <p>Add a task with specific beginning time and end time.</p><p>The default date when neither is provided is the next closest date where the end time is in the future.</p><p>The default end_date if start_date is provided is the closest date based on the end_time such that the event ends after it starts.</p><p>The default end_time is 1hr after the start time.</p> |
| [add] &lt;cmd&gt; every &lt;interval&gt; | <p>Make the event/task recurring.</p><p>&lt;cmd&gt; can be either a event or a deadline task command, as above.</p><p>The event/task will repeat itself every interval starting from the time stated in &lt;cmd&gt;.</p><p>Entering a floating task as &lt;cmd&gt; will cause the task to be transformed into a deadline task.</p> |


> You can leave parts of date/time field empty, LifeTracker will find the appropriate dates automatically

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

Date is specified in DDMMYY or DDMMYYYY format, separators are optional. Year, or year and month can be omitted. If so they are replaced by the current month/year. However, a lot of other things work as well. Try `today`,`tuesday`, or even `day after tomorrow`. All these works! The date and time input is very, very flexible. So use your imagination:)

Similarly, time can be specified in either 24-hr format e.g. `2359` or 12-hr format with 'am' or 'pm', e.g. `1259pm`. Separators are optional, and minutes can be omitted, to be replaced with 00 by default.
