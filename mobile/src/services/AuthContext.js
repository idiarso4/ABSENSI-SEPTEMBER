import React from 'react';

export const AuthContext = React.createContext({
  signOut: async () => {},
  signIn: async (_token) => {},
});
