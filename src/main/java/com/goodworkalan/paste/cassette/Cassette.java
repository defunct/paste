package com.goodworkalan.paste.cassette;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * This cassette is given to the {@link Controller} and acts as the interface
 * between the Servlet implementation package and the domain-specific language
 * package that implements the builder. Keeping the domain-specific language in
 * the same package as the servlet really clutters the package. The separation
 * of the builder from its product is a natural separation that makes the code
 * easier to document and comprehend.
 * <p>
 * This cassette is the bridge between between the builder and the product. The
 * Paste Servlet {@link Filter}, when it is created, creates a
 * <code>Cassette</code>, presses it into a <code>Connector</code>, and then
 * gives the <code>Connector</code> to the <code>Router</code> implementations
 * that define the application. The mutable containers within the cassette can
 * only be manipulated through the domain-specific language provided by the
 * <code>Connector</code> and its participants.
 * <p>
 * There is much that will trouble the pedantic Java programmer, so I'll address
 * the criticisms I anticipate one by one.
 * <p>
 * As noted above, encapsulation is accomplished by using the
 * <code>Connector</code> as a Fascade to the <code>Cassette</code>. The
 * application defined <code>Router</code> can only edit the
 * <code>Cassette</code> through the domain-specific language. The
 * <code>Filter</code> uses the output of the <code>Cassette</code> as its
 * internal state, so it does not need to hide the state of the
 * <code>Cassette</code> from itself.
 * <p>
 * Encapsulation is present in practice, however the details of the
 * implementation are not hidden from the user through the Java visibility
 * mechanisms. A user could create their own <code>Cassette</code>, when there
 * is no reason for them to do so. A cause for conniptions, until you reason
 * that, the construction of an object for which you have no use is not really a
 * particularly dangerous exposure of implementation details. This package is
 * documented as an internal package.
 * <p>
 * If the ability to instanciate internal objects is offensive, then consdier
 * the many dependent libraries that any complicate project employs for its
 * implementation. The barely functioning application developer from whom we
 * need to hide any widget that might distract their muddled minds is already
 * exposed to an enormous assortment of classes that they can instantiate and do
 * nothing with. Consider any framework that uses byte code trickery. For the
 * application developer, this is almost certainly an implementation detail, but
 * one that is exposed. That is, in my accounting application, using an
 * object-relational mapping layer, that uses bytecode manipulation to create
 * object graphs from the contents of a relational database, there had better
 * not be any byte code manipulation code in the account credit and debit
 * packages. Even though the classes of the byte code library are exposed,
 * because they must be present for or object-relational mapping library, we
 * shouldn't be using byte code manipulation directly for accounting tasks.
 * <p>
 * Which is why the hand-wringing about hiding seems silly, because if you can
 * extract that naughty bit of bare implementation detail into a separate
 * package, then the exposure somehow disappears. It's not hidden at all, but
 * not immediately visible in the current project. Out of sight, out of mind.
 * <p>
 * Why convolute your code to placate programmers so childish, they've yet to
 * develop object permanence? It pains me to see boldfaced letters saying that a
 * constructor is not supposed to be called. If an object is a conduit between
 * two sub-systems, then it is really is pointless for the application developer
 * to play with objects in this way. I'm struggling to recall an instance as a
 * child, where you figured out you could initiate something, but had no ability
 * to go anywhere with it. If the family station wagon has been left unlocked,
 * you can sit in the car, and turn the wheel a little bit, and move the
 * shifter, and press the gas, but you are no closer to making off with it.
 * <p>
 * In fact, using a system that allows you to build many small packages, such as
 * Cafe, you can remove the shinny objects entirely, and push them into a
 * supporting package, and then define that supporting package with a separate
 * batch of Javadoc, which will go a long way to defeating the accusation that
 * you've "violated encapsulation" by virtue of providing your library with a
 * set of services that are only useful to your library.
 * <p>
 * This is a pattern that I call the Cassette pattern or perhaps this is the
 * P.O. Box pattern, where you have a cubbyhole and it is set up in such a way
 * that you can only push things into it from one end and take them out from the
 * other. This isn't enforced by the cubbyhole itself, but wall in which you
 * install the cubbyhole and the doors to the rooms on either side.
 * <p>
 * The next great concern for a pedantic Java programmer is that any class as
 * trivial as this one is a candidate for immutability and the fact that is
 * mutable is a design flaw. Never mind that the class has a specific purpose
 * and that in fulfilling that purpose, it will never cross a thread boundary.
 * You can future-proof your library by pouring on indirections to designing for
 * use cases that have no conceivable benefit, but are technically possible.
 * <p>
 * Here, however, the mutability of the cubbyholes are part of the design. The
 * domain-specific language builds an object graph, and creating such a graph in
 * a fashion that is immutable is matter of making poor Java into something she
 * is not. At a more superficial level, you could attempt to split a
 * <code>CassetteBuilder</code> out of this <code>Cassette</code>, because when
 * you are looking at a structure, the split-structure, builder/product pattern
 * is easy to apply, but it sends a ripple of complexity throughout the library
 * as the clients of the builder/product split-structure now have to maintain a
 * mutable reference to an object that they keep rebuilding and replacing. It
 * looks more immutable, because you have one reference that you reassign, to an
 * immutable object, but you've made the client object no less immutable and
 * much more complicated. Instead of a simple assignment, you are now creating a
 * builder, calling a setter, then performing an assignment, for each property
 * that the builders update.
 * <p>
 * If it is mutable, it is mutable, and you're not going to get rid of the
 * mutability with the childish strategy toward peas of pushing them to the edge
 * of your plate.
 * <p>
 * It occurs to me that an anti-pattern, such as the needlessly split out
 * builder, is better expressed as a pattern, where the anti-pattern is the
 * problem and the pattern is the solution, and the solution is probably
 * something painfully simple, such as the Structure Pattern. You have a bit of
 * data that is bandied about by a number of participants, so rather than try to
 * attach the behavior to the data, you make place the data in a Structure and
 * treat it as a microscopic in-memory database.
 * <p>
 * Finally, for those people who are extending Paste with an implementation of
 * <code>Renderer</code>, the <code>Cassette</code> can be used to mock the
 * creation of the <code>Renderer</code>. Thus, there is no need for the
 * application developer to create a <code>Cassette</code>, but the exposure of
 * this service is a service to the test suites of <code>Renderer</code>
 * implementations.
 * <p>
 * Information hiding becomes tedious. It feels like you're at the bank and
 * teller is trying to impress everyone by processing transactions with his eyes
 * closed. We're talking about data in a language that favors assignment.
 * 
 * @author Alan Gutierrez
 */
public final class Cassette {
    /** A map of controller classes to the globs that define their URL bindings. */
    public Map<Class<?>, Path> routes;

    /**
     * A list of connection groups, a connection group consisting of URL
     * bindings. URL bindings consisting of a list of globs paired with rule map
     * of tests to further winnow the match based on request parameters.
     */
    public List<ConnectionSet<List<Connection>>> connections;
    
    /** A rule map to match a controller or exception to a renderer. */
    public RuleMapBuilder<BindKey, List<InjectorBuilder>> renderers;
    
    /** The map of reaction types to controllers. */
    public IlkAssociation<Class<?>> reactionsByType;

    /** The reaction annotations to controllers. */
    public Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation;

    /** The type to interceptor association. */
    public IlkAssociation<Class<?>> interceptors;
}
