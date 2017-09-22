package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator()
  {
    public FragmentState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FragmentState(paramAnonymousParcel);
    }
    
    public FragmentState[] newArray(int paramAnonymousInt)
    {
      return new FragmentState[paramAnonymousInt];
    }
  };
  final Bundle mArguments;
  final String mClassName;
  final int mContainerId;
  final boolean mDetached;
  final int mFragmentId;
  final boolean mFromLayout;
  final boolean mHidden;
  final int mIndex;
  Fragment mInstance;
  final boolean mRetainInstance;
  Bundle mSavedFragmentState;
  final String mTag;
  
  public FragmentState(Parcel paramParcel)
  {
    this.mClassName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    boolean bool2;
    boolean bool3;
    label70:
    boolean bool4;
    if (paramParcel.readInt() != 0)
    {
      bool2 = bool1;
      this.mFromLayout = bool2;
      this.mFragmentId = paramParcel.readInt();
      this.mContainerId = paramParcel.readInt();
      this.mTag = paramParcel.readString();
      if (paramParcel.readInt() == 0) {
        break label126;
      }
      bool3 = bool1;
      this.mRetainInstance = bool3;
      if (paramParcel.readInt() == 0) {
        break label132;
      }
      bool4 = bool1;
      label86:
      this.mDetached = bool4;
      this.mArguments = paramParcel.readBundle();
      if (paramParcel.readInt() == 0) {
        break label138;
      }
    }
    for (;;)
    {
      this.mHidden = bool1;
      this.mSavedFragmentState = paramParcel.readBundle();
      return;
      bool2 = false;
      break;
      label126:
      bool3 = false;
      break label70;
      label132:
      bool4 = false;
      break label86;
      label138:
      bool1 = false;
    }
  }
  
  public FragmentState(Fragment paramFragment)
  {
    this.mClassName = paramFragment.getClass().getName();
    this.mIndex = paramFragment.mIndex;
    this.mFromLayout = paramFragment.mFromLayout;
    this.mFragmentId = paramFragment.mFragmentId;
    this.mContainerId = paramFragment.mContainerId;
    this.mTag = paramFragment.mTag;
    this.mRetainInstance = paramFragment.mRetainInstance;
    this.mDetached = paramFragment.mDetached;
    this.mArguments = paramFragment.mArguments;
    this.mHidden = paramFragment.mHidden;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Fragment instantiate(FragmentHostCallback paramFragmentHostCallback, Fragment paramFragment, FragmentManagerNonConfig paramFragmentManagerNonConfig)
  {
    if (this.mInstance == null)
    {
      Context localContext = paramFragmentHostCallback.getContext();
      if (this.mArguments != null) {
        this.mArguments.setClassLoader(localContext.getClassLoader());
      }
      this.mInstance = Fragment.instantiate(localContext, this.mClassName, this.mArguments);
      if (this.mSavedFragmentState != null)
      {
        this.mSavedFragmentState.setClassLoader(localContext.getClassLoader());
        this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
      }
      this.mInstance.setIndex(this.mIndex, paramFragment);
      this.mInstance.mFromLayout = this.mFromLayout;
      this.mInstance.mRestored = true;
      this.mInstance.mFragmentId = this.mFragmentId;
      this.mInstance.mContainerId = this.mContainerId;
      this.mInstance.mTag = this.mTag;
      this.mInstance.mRetainInstance = this.mRetainInstance;
      this.mInstance.mDetached = this.mDetached;
      this.mInstance.mHidden = this.mHidden;
      this.mInstance.mFragmentManager = paramFragmentHostCallback.mFragmentManager;
      if (FragmentManagerImpl.DEBUG) {
        Log.v("FragmentManager", "Instantiated fragment " + this.mInstance);
      }
    }
    this.mInstance.mChildNonConfig = paramFragmentManagerNonConfig;
    return this.mInstance;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeString(this.mClassName);
    paramParcel.writeInt(this.mIndex);
    int j;
    int k;
    label68:
    int m;
    if (this.mFromLayout)
    {
      j = i;
      paramParcel.writeInt(j);
      paramParcel.writeInt(this.mFragmentId);
      paramParcel.writeInt(this.mContainerId);
      paramParcel.writeString(this.mTag);
      if (!this.mRetainInstance) {
        break label125;
      }
      k = i;
      paramParcel.writeInt(k);
      if (!this.mDetached) {
        break label131;
      }
      m = i;
      label84:
      paramParcel.writeInt(m);
      paramParcel.writeBundle(this.mArguments);
      if (!this.mHidden) {
        break label137;
      }
    }
    for (;;)
    {
      paramParcel.writeInt(i);
      paramParcel.writeBundle(this.mSavedFragmentState);
      return;
      j = 0;
      break;
      label125:
      k = 0;
      break label68;
      label131:
      m = 0;
      break label84;
      label137:
      i = 0;
    }
  }
}
