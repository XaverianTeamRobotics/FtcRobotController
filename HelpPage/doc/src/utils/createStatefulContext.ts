import { createContext, Dispatch, SetStateAction } from "react";

/**
 * <p>
 * Creates a context which can hold values provided by useState().
 * </p>
 * <p>
 * Oftentimes, one needs to create a stateful context outside a component to be able to export it. The problem, however, is that you now either need to create a complex type for the context or risk it being nullable because you can only make it stateful with useState() inside a component. This function provides a context with the horrible type signature needed to have a stateful value while also being null-safe.
 * </p>
 * <br><br>
 * @example Creating the context
 * const StatefulContext = createStatefulContext("Hello World!");
 * <br>
 * @example Using the context value
 * // the context will always return a value and reducer pair like useState()
 * // if the context has not been provided with a stateful value, the reducer will do nothing
 * const [ value, reducer ] = useContext(StatefulContext);
 * <br>
 * @example Providing a stateful value
 * <StatefulContext.Provider value={useState("Goodbye World!")}> // the provided context value may change
 *   { children }
 * </StatefulContext.Provider>
 * <br>
 * @param initial The initial, non-stateful value. Stateful values must be wrapped in useState() and passed to the provider.
 */
export const createStatefulContext = <T>(initial: T) => {
  return createContext<[T, Dispatch<SetStateAction<T>>]>(
    [
      initial,
      (value) => {
        return;
      }
    ]
  );
};