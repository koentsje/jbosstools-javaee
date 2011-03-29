package org.domain.SeamWebWarTestProject.session;

import javax.ejb.Remove;
import javax.ejb.Stateful;

import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;

@Stateful
@Name("StatefulDuplicateRemoveComponent")
public class StatefulDuplicateRemoveComponent {

    @Destroy
	public void destroyMethod(){
	}

    @Remove
    public void removeMethod1(){
    }

    @Remove
    public void removeMethod2(){
    }
}