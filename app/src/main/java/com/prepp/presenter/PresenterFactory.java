package com.prepp.presenter;


import com.prepp.dataService.DataServiceFactory;

/**
 * Created by Mohit Sharma on 26/8/15.
 */
public class PresenterFactory {

    private static PresenterFactory mPresenterFactory;
    private UserPresenter mUserPresenter;

    public static PresenterFactory getInstance(DataServiceFactory dataServiceFactory) {
        if (mPresenterFactory == null) {
            mPresenterFactory = new PresenterFactory(dataServiceFactory);
        }
        return mPresenterFactory;
    }

    private PresenterFactory() {
    }

    public PresenterFactory(DataServiceFactory dataServiceFactory) {
        mUserPresenter = new UserPresenter(dataServiceFactory.getUserDataService());
    }

    public UserPresenter getUserPresenter() {
        return mUserPresenter;
    }

}
