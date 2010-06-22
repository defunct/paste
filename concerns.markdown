---
layout: default
title: Paste Concerns and Decisions
---

# Paste Concerns and Decisions

General concerns about paste.

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
