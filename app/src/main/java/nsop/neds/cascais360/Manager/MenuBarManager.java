package nsop.neds.cascais360.Manager;

public class MenuBarManager {

    private static SessionManager sm;

   /* public static void CallWeather(Context context, NavigationView nav_view){
        new WeatherManager(context, nav_view).execute(WebApiCalls.getWeather());
    }

    public static boolean NavigationItemSelected(Context context, int id, DrawerLayout drawer){

        if(id == R.id.nav_login){
            context.startActivity(new Intent(context, LoginActivity.class));
        }else if (id == R.id.nav_home) {
            context.startActivity(new Intent(context, MainActivity.class));
        }else if (id == R.id.nav_definitions) {
            context.startActivity(new Intent(context, DefinitionsActivity.class));
        } else if (id == R.id.nav_agenda) {
            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("Type", "agenda");
            context.startActivity(intent);
        } else if (id == R.id.nav_visit) {
            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("Type", "visit");
            context.startActivity(intent);
        } else if (id == R.id.nav_routes) {
            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("Type", "routes");
            context.startActivity(intent);
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("type", "search");
            context.startActivity(intent);
        } else if (id == R.id.nav_calendar) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("type", "calendar");
            context.startActivity(intent);
        }else if (id == R.id.nav_map) {
            Intent intent = new Intent(context, SearchActivity.class);
            intent.putExtra("type", "maps");
            context.startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void SetUserSettings(final Context context, NavigationView navigationView){

        if(sm == null) {
            sm = new SessionManager(context);
        }

        View navHeader = navigationView.getHeaderView(0);

        if(sm.asUserLoggedOn()) {
            navHeader.setVisibility(View.VISIBLE);
            ImageView usericon = navHeader.findViewById(R.id.user_icon_image);
            TextView username = navHeader.findViewById(R.id.user_name);
            username.setText(sm.getDisplayname());

            navigationView.getMenu().setGroupVisible(R.id.menu_gpr1, false);

            navigationView.getMenu().getItem(8).setVisible(true);

            ImageView logout = navHeader.findViewById(R.id.logout_icon);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(AccountGeneral.logout(context)){
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            });

            usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    context.startActivity(intent);
                }
            });

        }else{
            navHeader.setVisibility(View.GONE);
            navigationView.getMenu().setGroupVisible(R.id.menu_gpr1, true);
        }
    }*/
}
