# Twiddles

## Request Method and Parameters for Reactions

Thought that I could treat reactors just like another set of controllers, but
turns out that Invoke uses Annotations object, which expects an
HttpServletRequest and the Controller qualified Parameters.

Should I go and bind some nonsense to the Controller qualified parameters, and
then create a mock HtttpServletRequest? Would it serve me to allow the injector
to serve up nulls with an unimplemented provider?

I dicided that, since the only thing that Annotations cares about is the request
method, to create a Verb qualifier for a String and then create a default
constructor for Parameters, so that Annotations can be constructed with some
reasonable defaults.

It occurs to me that, in the request, the verb and parameters and path are the
only components that can be relied upon, since the client needs them to identify
the resource requested, so there is no need to continue and create a ContentType
qualifier and the like.
