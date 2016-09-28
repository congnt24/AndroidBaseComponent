# AndroidBaseComponent
Provide a convinient way to develop an android application. It's contain all utilities that you need, some custom layout and rapid way to implement Activity...
## Getting Started
To implement it, just download/clone to your computer and import like a module
## How to use
### 1. Activity
```java
@Activity(fullscreen = true,
        transitionAnim = Activity.AnimationType.ANIM_BOTTOM_TO_TOP,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM,
        mainLayoutId = R.layout.activity_main,
        enableSearch = true)
@NavigationDrawer
public class MainActivity extends AwesomeActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new CustomActionbar(this);
    }

    @Override
    protected void initialize(View mainView) {} 
}
```
### 2. ActionBar
#### Default Search Bar
```java
@ActionBar(actionbarType = ActionBar.ActionbarType.DEFAULT_SEARCH)
public class CustomActionbar extends AwesomeActionBar {
    public CustomActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}
```
#### Custom Search Bar
```java
@ActionBar(actionbarType = ActionBar.ActionbarType.MATERIAL_SEARCH)
public class CustomActionbar extends AwesomeActionBar {
    public CustomActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}
```
#### Floating Search Bar
```java
@ActionBar(actionbarType = ActionBar.ActionbarType.FLOATING_SEARCH)
public class CustomActionbar extends AwesomeActionBar {
    public CustomActionbar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}
```
![AndroidBaseUtils Art Image](screenshot/001.png)
