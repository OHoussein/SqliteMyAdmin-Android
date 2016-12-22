### A debug tool for Android that inspects the database of the running app
#### Features
* List of the databases
* List of the tables
* List of entries
* Execute custom query

#### Usage
```
Add it in your root build.gradle at the end of repositories:
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
Add the dependency
```
dependencies {
    ...
    compile 'com.github.ohoussein.sqlitemyadmin:sqlitemyadmin:1.0.0'
    }
```
* add a debug button in your app
* then just call ```SqliteMyAdminActivity#navigate``` on onClickListener.

#### Showcases
<p  align="center">
<img src="https://raw.githubusercontent.com/OHoussein/SqliteMyAdmin-Android/master/media/list_db.png" alt="List of databases" />
<p align="center"><b>List of databases</b></p>
</p>

<p  align="center">
<img src="https://raw.githubusercontent.com/OHoussein/SqliteMyAdmin-Android/master/media/list_tables.png" alt="List of tables" />
<p align="center"><b>List of tables</b></p>
</p>

<p  align="center">
<img src="https://raw.githubusercontent.com/OHoussein/SqliteMyAdmin-Android/master/media/entries.png" alt="List of entries" />
<p align="center"><b>List of entries</b></p>
</p>

<p  align="center">
<img src="https://raw.githubusercontent.com/OHoussein/SqliteMyAdmin-Android/master/media/custom_query.png" alt="Custom query result" />
<p align="center"><b>Custom query result</b></p>
</p>

</p>
