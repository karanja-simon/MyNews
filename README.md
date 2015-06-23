# MyNews
RSS (Rich Site Summary); originally RDF Site Summary; often called Really Simple Syndication, uses a family of standard web feed formats[2] to publish frequently updated information: blog entries, news headlines, audio, video. An RSS document (called “feed”, “web feed”,[3] or “channel”) includes full or summarized text, and metadata, like publishing date and author’s name.

RSS feeds enable publishers to syndicate data automatically. A standard XML file format ensures compatibility with many different machines/programs. RSS feeds also benefit users who want to receive timely updates from favourite websites or to aggregate data from many sites. Read More (Wikipedia)

This is Java SE back-end and Java Swing UI application, that works by parsing RSS (Really Simple Syndicate)  from the abc news  feed. (Am currently working on multiple feeds). The heart of the application is the ROME XML parser, that reads the feed output xml file and parses the tags to generate the new content and metadata. Once the feed is stripped off the xml tags, then the news content is simply passed to the UI to be displayed.

The UI is built from a combination of default Java Swing components and the WebLaf Looks for the drop-down menu and the combobox. Below is a typical view of the application running on Windows 7.
<p>
<img src="https://www.dropbox.com/s/a9mplqp5uriwvko/mynews.png?raw=1">
</p>
