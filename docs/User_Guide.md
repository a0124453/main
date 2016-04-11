# User Guide

## Table of Contents

 1. Quick Start
 2. Feature Details


## Quick Start

### Setup

 1. Download and install Java 8 (JRE is fine), from [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).
 2. Download the .jar file from our [releases page](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).
 3. Place the .jar file at a convenient location and double-click it to run!

### Usage

 1. Try adding some simple entries, like:  
    - `Submit report by 7pm`
    - `Movie from 2pm to 3pm tmr`
    - `Team meeting from wed 2pm to 3pm every week`
    - The command `add` can be used too for special cases, but is optional otherwise.
     - `add add header to report`
 2. If you make a mistake, `undo` and `redo` will set things right.
    - You can also scroll through what you typed with the up/down arrow keys.
 3. When your done with something, simply mark it based on it's ID to archive it. Recurring entries like "Team meeting" will be advanced automatically.  
    - `mark 3`
 4. You can search for entries with `find`.
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
 - Horizontal bar: `opt1 | opt2` means either `opt1` or `opt2` can be specified.

### Adding entries

The common form of all add command entry is:

```
[add]<task>[KeywordPhrase]
```

where the list of possible forms of `KeywordPhrase` can be found below.
> Although the word `add` is optional, if you required the word `add` to be part of your task name, do type `add add...` to identify it as part of the name. This applies to other command keywords such as `edit`, `find`, etc. as well.

| KeywordPhrase | Description |
| ------------- | ----------- |
| NULL | When no phrase is given, a floating task will be created with no deadlines |
| by [date and time] | <p>Add a task with a deadline specified by `date and time` field.</p><p>If the `date and time` field is empty after the `by` keyword, the deadline defaults to the day itself at 2359.</p> |
| at | Alternative keyword to "by". Works the same way but cannot be used concurrently. |
| from [date and time] | <p>Add an event with a start date specified by `date and time` field.</p><p>If the `date and time` field is empty after the `from` keyword, the start date/time defaults to the day itself at nearest hour to the current time.</p><p>MUST NOT be used concurrently with `by` or `at` keyword.</p> |
| to [date and time] | <p>Add an event with an end date specified by `date and time` field.</p><p>If the `date and time` field is empty after the `to` keyword and the start date/time is not specified, the end date/time defaults to the day itself at nearest hour to the current time. Otherwise, the end date/time defaults to one hour after the start date/time.</p><p>Should be used concurrently with `from` keyword. MUST NOT be used concurrently with `by` or `at` keyword.</p>
| every <period> | <p>Make the event/task recurring with the interval specified by `period` field.</p><p>Can be used with any other keywords above.</p> |
| until <date> | <p>Set the date limit for recurring events/tasks as specified by `date` field.</p><p>MUST only be used with `every` keyword.</p> |
| for <number of times> | <p>Set the number of times for recurring event/task to be repeated.</p><p>MUST only be used with `every` keyword. MUST NOT be used concurrently with `until` keyword.</p> |
> The order of the keywords does not matter e.g. `dinner from 7pm to 9pm` works the same way as `dinner to 9pm from 7pm`.  
You can leave parts of `date and time` field empty; LifeTracker will find the appropriates values automatically.

### Editing entries

The form of an edit command is as follows:  
```
edit <ID number> > <KeywordPhrase>
editone <ID number> > <KeywordPhrase>
```
Note the '>' symbol in between the fields `ID number` and `KeywordPhrase`. `editone` is used to edit only one instance of a recurring task/event.  
  
All forms of `KeywordPhrase` mentioned in the preceding section for adding entries apply to this section, on top of the following additional ones:

| KeywordPhrase | Description |
| ------------- | ----------- |
| stop | Changes a recurring task/event into a non-recurring one. |
| forever | Removes any occurrence limit or date limit on the recurring task/event. |
| no due | Changes the entry into a floating task. |

### Searching for entries

`find` is the search command, and works by filters. A typical search command takes one of the following forms:

```
find submit
today
```

This command lists all tasks/events with the word "submit" in it. Don't be worried about minor typos; LifeTracker has it covered. A list of all possible search commands is found below.

| Command | Description |
| ------- | ----------- |
| find [word] | Displays entries that contain `word` in it. Shows all entries if `word` is not specified. |
| findall [word] | Displays all entries, including archived ones, that contain `word` in it. Shows all entries if `word` is not specified. |
| findold [word] | Displays archived entries that contain `word` in it. Shows all archived entries if `word` is not specified. |
| today | Displays all floating tasks, tasks that are due today, and events that either start today or are ongoing. |
| todayall | Displays all floating tasks, tasks that are due today, and events that either start today or are ongoing, including archived ones. |
| todayold | Displays only archived floating tasks, tasks that are due today, and events that either start today or are ongoing. |

> All instances of `find` can be replaced by `list` or `search`, eg. `listall`, `searchall`, `searchold`, etc.

### Deleting entries

`delete` removes tasks/events via its assigned ID number, which is shown in the ID column of the LifeTracker app.

| Command | Description |
| ------- | ----------- |
| delete <ID> | Permanently deletes the entry with the given ID number. |

### Marking entries as done
Once you are done with a task, you can use the command `mark <ID>` to archive it. Recurring entries will have their relevant dates/times updated, and a duplicate entry will also be added to the archive.

### Miscellaneous commands

| Command | Description |
| ------- | ----------- |
| undo | Undo the last operation. |
| redo | Redo the last operation. |
| saveat &lt;dir/file&gt; | Sets the file the program saves to. |
| exit | Exits LifeTracker. |

### Date and Time

Date is specified in DDMMYY or DDMMYYYY format. Separators are optional. Year, or year and month can be omitted. If so they are replaced by the current month/year. However, a lot of other key phrases work as well. Try `today`,`tuesday`, or even `day after tomorrow`. The date and time input is very flexible. So use your imagination.

Similarly, time can be specified in either 24-hr format e.g. `2359` or 12-hr format with 'am' or 'pm', e.g. `1259pm`. Separators are optional, and minutes can be omitted, to be replaced with 00 by default.
