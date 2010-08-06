---
layout: default
title: Paste Concerns and Decisions
---

# Paste Concerns and Decisions

General concerns about paste.

## Too Many Annotations

Now that I have the mappings for a controller, along with the `Path` and
`Suffix`, maybe it is time to create a `Binding` class or a `Match` class and
have that contain this properties? Then you have a type to lock onto, not so
many tiny little components from the match.

This will also allow for a null suffix. It will allow for null path and suffix
when the controller is invoked as a reaction. The naming of this item is going
to take some time to get right.

Will we do this?

## Missing Suffixes

Curious case of the needed file suffix. 

Needed to get the suffix since the controller merely reads a file off of the
file system. There is no content type based on file suffix going on. The
controller makes these determinations itself.

I've created a `Suffix` annotation and will add the matched suffix to the
parameters bound in controller.

One solution would be to provide a flag to the path definition that would tell
it to include the suffix in the path. This could come afterward too, as a
modifier, includeSuffix(), which would be syntactically easier on the eyes.

Another solution would be to extend the Dovetail pattern matching language to
specify whether you're matching suffixes or not, perhaps with wildcard
characters, or maybe you need to be explicit about the regular expression? 

The default it to match the path without the suffix. That feels natural.

Please consider this and resolve this issue soon.

## Renderer Matching Exceptions

If you specify no exception, which is the common case, is it explicit?
Otherwise, you need to remember to place exception matching above regular
matching. There is a case for you to try out here.

## Stream Encodings

How do you include the encodings that come out of streams? You are doing
include, using streams, but that means that the same Response is passed, not a
wrapped one, and so..

And so, to my mind, the output methods should all use the `OutputStream` and
never the writer, and always force people to choose an encoding, and if none is
specified, fall back to UTF-8.

For your includes, this means you need to make sure that the files you include
are encoded in UTF-8, which I believe they are. What is the Java default
encoding? Do you fall back to that or UTF-8? The Java default encoding will tell
you what the encoding is on the file system.

## Web Application Structure

What is a good application structure for a Paste application?

## Annotations and Invoke

I wanted to use controllers to get the ball rolling with reactions, but now I'm
starting to wonder about that. I wanted to use the Invoke action, Actors, but
when I did, I reailized that it uses the HTTP request.

*Twiddle*

So, I twiddled the code. And thus, I begin to twiddle.

## Render Matching

Wonder if I can't just reused the path statement?

## Authorization

Probably can put an annotation on the controller, or on the controller methods,
ah, no we want to intercept before we reach the methods. The nested controller
format is good. Then use an interceptor.

## Invoke Parameters

Any value to annotating invoked method parameters with a parameter to pass into
the method from the parameter string?

## DSL

Domain-specific languages, like the Paste connector language, make is really
rediculous to document, if Javadoc must indicate what takes place in with a
method, the contents of a Paste connect method are so complex, it makes no sense
to attempt to duplicate that complexity in the Javadoc, not with prose, not by
describing a "contract."

## Usage

I'm documenting filter and request scope and realizing that the `PasteFilter`
will only be used as an all encompasing filter, that you cannot forward to the
paste filter, because it will not detect the forward parameters, if it has not
seen the client request.

I don't really have this sorted yet:

http://www.caucho.com/resin-3.0/webapp/faq.xtp#include

Need to write tests for Jetty, use that as a baseline.

## Constructing

Looks like I've got more than one construction pattern. There is dependency
injection, but there is also Stash as resource locator. Stash is used by String
Beans JPA.

Now I'm about to do String Beans Validation, and I'm going to need to get my
hands on an EntityManager. I thought about building a validation engine every
time one is needed, like a `Provider` that wil build an instance of a validator,
and the provider can request the `EntityManager`, but then I'm opening a
database connection everytime I neeed validation, whether or not that validation
is against the database.

One could just go and roll dependency injection into String Beans.

Hey, that's not a bad idea, is it? It means that String Beans is always at least
100k, because of the dependency injection. Part of String Beans is simplicity
and not creating the beans themselves except througha default constructor.
Adding dependency injection to create pariticipants invites the construction of
the beans themselves using dependency injection.

Also, consider the costs, adding DI to the core of String Beans, when it is not
likely needed for a lot of reasonable applications of String Beans.

Thus, the Stash is not a bad way to go, if it is what you introduce in order to
add an unforeseen participant or two. That is, for things like String Beans, I
can see only the need for a database handle at times. Validation could
refernence existing data, but it won't need things like routes or HTTP request
object.

Now I'm realizing that by virtue of requesting it for the Stash, I'm
constructing the `EntityManager` anyway. In fact, it is unlikely that a request
to a database web application will not need to open a database connection 80% of
the time. Why not just build your validator straight up then?

I'm looking at EntityManagerFactory and wondering why I can't just have a
provider interface exposed, then while I'm at it, why do I need a Stash? Can't I
just use a special sort of key with a map? Let's say, a list that has a unique
object and a class, and that will return an object? That object can be an
Ilk.Box and the key can be a Ilk.Key. That would get worked into String Beans.

Then it becomes one of those, if it hurts, don't do that. Why am I adding all
these interfaces, all this bluk, to prevent an API implementor from screwing
around with a map? What is to keep them from changing the system locale to
Moldovia?

Funny how you grow less enamored of the little twiddles the further along you
are with larger concepts. In any case, I'm pretty sure I can remove the
dependency on Ilk, at this point, I absoutely do not need it. Maybe, I can add a
dependency on javax to get the Provider interface. (Oh, but that means generics,
foo.) Cheap lazy construction is a problem. Maybe a problem for another time.

## Routes

An interesting refactor...

I'd developed a `Routes` interface that had one and only one implementation
`CoreRoutes`. That implementation was in the `.servlet` package, so that the
`Routes` interface exposed no constructor and, as they say, the implementation
details were entirely hidden.

The `Routes` interface provided a way for one to generate a URL path from a
controller class and a map of parameters for the identifiers in the Dovetail
`Path`, a way to get a URL path given a controller, for redirections, hyperlinks
and the like.

The implementation was so simply have an underlying map from a controller to a
Dovetail `Path`. The hidden detail was that the constructor took a just this
sort of map and obviously held a reference to it. Thus, I've added doubled the
aritfacts in order to hide this mundane detail, that with a a defensive copy of
the map, exposes no potential harm to the implementation. That is, there is no
way to break the implementation and no need to call this constructor really, but
if you did, you'd have a working `Routes` instance that could generate URL paths
form the map you gave it. Where's the problem?

There is none.

And then I went one futher. Rather than build a `Routes` and add it to the
application injector, I've made `Routes` constructed through injection and
provided the map of controller classes to Dovetail `Path` instances using the
`Application` qualifier, so now `Routes`, when needed is constructed through
dependency injection.

Which is the shinny new bauble in the Java showcase, that depdendency injection,
and so it feels right, to, as they say, leverage dependency injection, and tell
the world how this is assembled by the injector. It is clever so it will
obviously get a pass form the nattering nabobs.

http://github.com/bigeasy/paste/commit/6e1cb3bdfef1f9ba684f74dab38e2ded5792bbda

## Controller Interface

Currently, controllers are just objects, POJOs, constructed through injection
and annotated in order to be of some use. There is no need for a controller to
implement any particular interface.

Although, if there were a `Controller` interface, it would be easier to identify
the single controller that exists in the lifetime of a request or a reaction.

Then I could migrate Ilk Inject to select the best matching interface, maybe
separate exact from assignable in the Ilk Inject interface.

## Reaction Scope

Am I still assigning things to the Reaction scope? How do I create scope
synonyms?

## Reactions

Currently you bind to a class, but shouldn't you bind instead to an annotation?
Something that is not as expensive to create in terms of byte size.

## Filtering and Interception

Interception is broken again. Each time the filter is invoked, a new interceptor
is created, but only the first interceptor is visible through the injector.
Can't get my head wrapped around what it means to intercept when the Paste
filter is invoked multiple times.

Do we need an interception flag for each invocation, or do we need just one
interception flag for the root of the thread local stack of calls?

When the filter flag is flipped, we no longer call `doFilter` and forward the
filtering, so is there a case where we need to invoke this multiple times, not
counting on just one flag? We want to say that if the filter generates output,
then we're not going to forward, so that the default action is not invoked.

The one place where it makes sense to say it needs to happen per filter is
during include. So, having an interceptor per invocation, makes that problem go
away.

Glad I have tests for all this.

## Getting Read to Remove Extensions

Suffixes ought to be stripped. It is time. It is what I expect.

It can be optional, I suppose, but that would be later. Now it will be one way
or another. For now, routes match without suffixes.

Hmm... Or I could specify a suffix regular expression, I suppose. That might be
a good switch. Because, I have already come up with a special case where id like
to have dots in the name.
