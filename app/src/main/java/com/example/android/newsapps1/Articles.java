package com.example.android.newsapps1;

public class Articles {

    //variable to store the section
    private String SectionVar;
    //variable to store the title
    private String TitleVar;
    //variable to store the Publishers name
    private String AuthorNameVar;
    //variable to store the date
    private String DateVar;
    //variable to store the url of each list item
    private String UrlVar;

    //public constructor for Articles class
    public Articles(String Section, String Title, String AuthorName, String Date, String Url) {
        SectionVar = Section;
        TitleVar = Title;
        AuthorNameVar = AuthorName;
        DateVar = Date;
        UrlVar = Url;
    }

    //get the section and return it's value
    public String getSection() {
        return SectionVar;
    }

    //get the title and return it's value
    public String getTitle() {
        return TitleVar;
    }

    //get the publishers name and return it's value
    public String getAuthorName(){
        return AuthorNameVar;
    }

    //get the date and return it's value
    public String getDate() {
        return DateVar;
    }

    //get the url of the item and return it's value
    public String getUrl() {
        return UrlVar;
    }
}
