export const makeAssertion = (assertion: boolean) => {
  if(!assertion) {
    throw new Error("Assertion failed!");
  }
};
