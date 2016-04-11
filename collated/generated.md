# generated
###### /src/lifetracker/ui/README.html
``` html
<h1 id="life-tracker">Life Tracker</h1>
<p>Keep your life together.</p>
<h2 id="table-of-contents">Table of Contents</h2>
<ol>
<li>About<br /></li>
<li>Authors<br /></li>
<li>Quick Start<br /></li>
<li>Feature Details</li>
</ol>
<h2 id="about">About</h2>
<p>Have you ever wanted to keep track of your deadlines and scheduling your events in just a simple one-line command? How about editing, searching, and finding the deadlines on-the-go?</p>
<p>Life Tracker does all that and more! It is a simple tool that will help you keep track of all your upcoming deadlines and events.. On top of being able to add/edit/update/delete your deadlines and events, you can see how long you have until your deadline. You can even save your schedule both online and offline to be able to access it anywhere!</p>
<h2 id="authors">Authors</h2>
<ul>
<li>Shen Yichen<br /></li>
<li>Nithi Chindaraksawong<br /></li>
<li>Muhammad Haikal Bin Rosman<br /></li>
<li>Shang Shuqi</li>
</ul>
<h2 id="quick-start">Quick Start</h2>
<ol>
<li>Press Ctrl + Spacebar to bring up Life Tracker.<br /></li>
<li>Try adding new deadline with a simple command. Here are some examples:<br /></li>
</ol>
<ul>
<li><code>Do homework by 7pm</code><br /></li>
</ul>
<ol>
<li>View your deadlines.<br /></li>
<li>Try more commands.</li>
</ol>
<p>To learn more details of Life Tracker features, refer to the 'Feature Details' section below.</p>
<h2 id="feature-details">Feature details</h2>
<h3 id="add">add</h3>
<p>The add command lets you add a task, like:</p>
<pre><code>add Water Plants by 6pm</code></pre>
<p>Variants can allow you to add events, with a start and end time, or tasks with no due date/time. See the table below.</p>
<p>Note that as a command, <code>add</code> is optional. Typing a command without the first keyword will automatically be considered an <code>add</code>.<br />Thus,</p>
<pre><code>Water Plants by 6pm</code></pre>
<p>will work too.</p>
<blockquote>
<p>[option] is an optional option, and option1|option2 means either option1 or option2 can be specified.</p>
</blockquote>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">[add] &lt;task&gt;</td>
<td align="left">Adds a floating task, with the name as specified in &lt;task&gt;</td>
</tr>
<tr class="even">
<td align="left">[add] &lt;task&gt; by [date] [time]</td>
<td align="left"><p>Adds a deadline task, by the name specified.</p><p>If no date is given, it is set to the next date where [time] is still in the future.</p><p>If no time is given, it is set to 2359.</p><p>If nothing is entered after the “by” keyword, deadline defaults to the day itself at 2359.</p></td>
</tr>
<tr class="odd">
<td align="left">[add] &lt;event&gt; from [start_date] &lt;start_time&gt; to [end_date] [end_time]</td>
<td align="left"><p>Add a task with specific beginning time and end time.</p><p>The default date when neither is provided is the next closest date where the end time is in the future.</p><p>The default end_date if start_date is provided is the closest date based on the end_time such that the event ends after it starts.</p><p>The default end_time is 1hr after the start time.</p></td>
</tr>
<tr class="even">
<td align="left">[add] &lt;cmd&gt; every &lt;interval&gt;</td>
<td align="left"><p>Make the event/task recurring.</p><p>&lt;cmd&gt; can be either a event or a deadline task command, as above.</p><p>The event/task will repeat itself every interval starting from the time stated in &lt;cmd&gt;.</p><p>Entering a floating task as &lt;cmd&gt; will cause the task to be transformed into a deadline task.</p></td>
</tr>
</tbody>
</table>
<h3 id="list">list</h3>
<p>List is the search command, and works by filters. E.g.:</p>
<pre><code>list water after 12/01</code></pre>
<p>lists all tasks/events with the word &quot;water&quot; and with dates associated that occur after 12th January this year.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">list [search_string] [on|after|before [date][time]]</td>
<td align="left"><p> List tasks that contains the text.</p><p>Returns everything if nothing is specified.</p><p>The on, after, before filters can each be specified once to filter for elements based on date and time.</p></td>
</tr>
</tbody>
</table>
<h3 id="delete">delete</h3>
<p>Delete removes tasks/events through searching for terms in it's name. If many matching candidates are found, they will be listed so you can choose which to delete.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">delete &lt;search_string&gt;</td>
<td align="left">Finds and delete a task. The user is prompted with a list on multiple matches.</td>
</tr>
</tbody>
</table>
<h3 id="edit">edit</h3>
<p>Edit can be used to update tasks/events. It also works by matching tasks/events with the term provided. Then, you can enter the edit string, which is in the exact same syntax as <code>add</code>. E.g.</p>
<pre><code>edit Water Plants &gt; Water Garden on 12/1</code></pre>
<p>will change the Water Plants tasks to be on 12th January.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">edit &lt;search_string&gt; &gt; &lt;cmd&gt;</td>
<td align="left"><p>Changes the tasks/event accordingly to &lt;cmd&gt;, which should take the syntax of any <code>add</code> command.</p><p>The defaults of <code>add</code> will be ignored if the tasks/event already have values in those fields.</td>
</tr>
</tbody>
</table>
<h3 id="miscellaneous">Miscellaneous</h3>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">undo</td>
<td align="left">Undo the last operation.</td>
</tr>
<tr class="even">
<td align="left">mark &lt;search_string&gt;</td>
<td align="left">Mark a task as done, same behaviour as delete.</td>
</tr>
<tr class="odd">
<td align="left">savedir &lt;dir/file&gt;</td>
<td align="left">Sets the file the program saves to.</td>
</tr>
<tr class="even">
<td align="left">exit</td>
<td align="left">Exit Life Tracker.</td>
</tr>
</tbody>
</table>
<h3 id="date-and-time">Date and Time</h3>
<p>Date is specified in DDMMYY or DDMMYYYY format, separators are optional. Year, or year and month can be omitted. If so they are replaced by the current month/year.</p>
<p>Time can be specified in 24-hr format with a 'h' suffix e.g. <code>2359h</code>. 12-hr time ends with either 'am' or 'pm', e.g. <code>1259pm</code>. Separators are optional, and minutes can be omitted, to be replaced with 00 by default.</p>
```
###### /bin/lifetracker/ui/README.html
``` html
<h1 id="life-tracker">Life Tracker</h1>
<p>Keep your life together.</p>
<h2 id="table-of-contents">Table of Contents</h2>
<ol>
<li>About<br /></li>
<li>Authors<br /></li>
<li>Quick Start<br /></li>
<li>Feature Details</li>
</ol>
<h2 id="about">About</h2>
<p>Have you ever wanted to keep track of your deadlines and scheduling your events in just a simple one-line command? How about editing, searching, and finding the deadlines on-the-go?</p>
<p>Life Tracker does all that and more! It is a simple tool that will help you keep track of all your upcoming deadlines and events.. On top of being able to add/edit/update/delete your deadlines and events, you can see how long you have until your deadline. You can even save your schedule both online and offline to be able to access it anywhere!</p>
<h2 id="authors">Authors</h2>
<ul>
<li>Shen Yichen<br /></li>
<li>Nithi Chindaraksawong<br /></li>
<li>Muhammad Haikal Bin Rosman<br /></li>
<li>Shang Shuqi</li>
</ul>
<h2 id="quick-start">Quick Start</h2>
<ol>
<li>Press Ctrl + Spacebar to bring up Life Tracker.<br /></li>
<li>Try adding new deadline with a simple command. Here are some examples:<br /></li>
</ol>
<ul>
<li><code>Do homework by 7pm</code><br /></li>
</ul>
<ol>
<li>View your deadlines.<br /></li>
<li>Try more commands.</li>
</ol>
<p>To learn more details of Life Tracker features, refer to the 'Feature Details' section below.</p>
<h2 id="feature-details">Feature details</h2>
<h3 id="add">add</h3>
<p>The add command lets you add a task, like:</p>
<pre><code>add Water Plants by 6pm</code></pre>
<p>Variants can allow you to add events, with a start and end time, or tasks with no due date/time. See the table below.</p>
<p>Note that as a command, <code>add</code> is optional. Typing a command without the first keyword will automatically be considered an <code>add</code>.<br />Thus,</p>
<pre><code>Water Plants by 6pm</code></pre>
<p>will work too.</p>
<blockquote>
<p>[option] is an optional option, and option1|option2 means either option1 or option2 can be specified.</p>
</blockquote>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">[add] &lt;task&gt;</td>
<td align="left">Adds a floating task, with the name as specified in &lt;task&gt;</td>
</tr>
<tr class="even">
<td align="left">[add] &lt;task&gt; by [date] [time]</td>
<td align="left"><p>Adds a deadline task, by the name specified.</p><p>If no date is given, it is set to the next date where [time] is still in the future.</p><p>If no time is given, it is set to 2359.</p><p>If nothing is entered after the “by” keyword, deadline defaults to the day itself at 2359.</p></td>
</tr>
<tr class="odd">
<td align="left">[add] &lt;event&gt; from [start_date] &lt;start_time&gt; to [end_date] [end_time]</td>
<td align="left"><p>Add a task with specific beginning time and end time.</p><p>The default date when neither is provided is the next closest date where the end time is in the future.</p><p>The default end_date if start_date is provided is the closest date based on the end_time such that the event ends after it starts.</p><p>The default end_time is 1hr after the start time.</p></td>
</tr>
<tr class="even">
<td align="left">[add] &lt;cmd&gt; every &lt;interval&gt;</td>
<td align="left"><p>Make the event/task recurring.</p><p>&lt;cmd&gt; can be either a event or a deadline task command, as above.</p><p>The event/task will repeat itself every interval starting from the time stated in &lt;cmd&gt;.</p><p>Entering a floating task as &lt;cmd&gt; will cause the task to be transformed into a deadline task.</p></td>
</tr>
</tbody>
</table>
<h3 id="list">list</h3>
<p>List is the search command, and works by filters. E.g.:</p>
<pre><code>list water after 12/01</code></pre>
<p>lists all tasks/events with the word &quot;water&quot; and with dates associated that occur after 12th January this year.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">list [search_string] [on|after|before [date][time]]</td>
<td align="left"><p> List tasks that contains the text.</p><p>Returns everything if nothing is specified.</p><p>The on, after, before filters can each be specified once to filter for elements based on date and time.</p></td>
</tr>
</tbody>
</table>
<h3 id="delete">delete</h3>
<p>Delete removes tasks/events through searching for terms in it's name. If many matching candidates are found, they will be listed so you can choose which to delete.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">delete &lt;search_string&gt;</td>
<td align="left">Finds and delete a task. The user is prompted with a list on multiple matches.</td>
</tr>
</tbody>
</table>
<h3 id="edit">edit</h3>
<p>Edit can be used to update tasks/events. It also works by matching tasks/events with the term provided. Then, you can enter the edit string, which is in the exact same syntax as <code>add</code>. E.g.</p>
<pre><code>edit Water Plants &gt; Water Garden on 12/1</code></pre>
<p>will change the Water Plants tasks to be on 12th January.</p>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">edit &lt;search_string&gt; &gt; &lt;cmd&gt;</td>
<td align="left"><p>Changes the tasks/event accordingly to &lt;cmd&gt;, which should take the syntax of any <code>add</code> command.</p><p>The defaults of <code>add</code> will be ignored if the tasks/event already have values in those fields.</td>
</tr>
</tbody>
</table>
<h3 id="miscellaneous">Miscellaneous</h3>
<table>
<thead>
<tr class="header">
<th align="left">Command</th>
<th align="left">Description</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td align="left">undo</td>
<td align="left">Undo the last operation.</td>
</tr>
<tr class="even">
<td align="left">mark &lt;search_string&gt;</td>
<td align="left">Mark a task as done, same behaviour as delete.</td>
</tr>
<tr class="odd">
<td align="left">savedir &lt;dir/file&gt;</td>
<td align="left">Sets the file the program saves to.</td>
</tr>
<tr class="even">
<td align="left">exit</td>
<td align="left">Exit Life Tracker.</td>
</tr>
</tbody>
</table>
<h3 id="date-and-time">Date and Time</h3>
<p>Date is specified in DDMMYY or DDMMYYYY format, separators are optional. Year, or year and month can be omitted. If so they are replaced by the current month/year.</p>
<p>Time can be specified in 24-hr format with a 'h' suffix e.g. <code>2359h</code>. 12-hr time ends with either 'am' or 'pm', e.g. <code>1259pm</code>. Separators are optional, and minutes can be omitted, to be replaced with 00 by default.</p>
```
