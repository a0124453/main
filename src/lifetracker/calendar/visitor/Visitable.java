package lifetracker.calendar.visitor;

import sun.reflect.generics.visitor.Visitor;

public interface Visitable {
    <T> T accept(Visitor<T> visitor);
}
