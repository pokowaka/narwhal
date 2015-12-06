package com.yayaway.narwhal.injection.components;


import com.yayaway.narwhal.com.yayaway.narwhal.ui.SubmissionAdapter;
import com.yayaway.narwhal.injection.PerFragment;
import com.yayaway.narwhal.injection.modules.FragmentModule;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Fragment-level components should extend this component.
 * <p/>
 * Subtypes of FragmentComponent should be decorated with annotation:
 * {@link PerFragment}
 */
@PerFragment
@Component(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(SubmissionAdapter adapter);
}