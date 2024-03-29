package nsop.neds.mycascais.Entities.Json;

import java.util.List;

public class Place {
    public int ID;
    public String CategoryTheme;
    public String WebURL;
    public String Title;
    public List<SubTitle> SubTitle;
    public String OnlineTicket;
    public List<Category> Category;

    public String CustomHours;
    public List<String> NextDates;
    public List<String> Images;

    public OfficeHours OfficeHours;

    public String Description;

    public List<Point> Points;

    public Price Price;

    public ItHappensHere ItHappensHere;

    public List<Tabs> Tabs;

    //public MoreInfo MoreInfo;
}
