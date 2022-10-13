function jwtHeaders(jwtToken) {
  return {headers: jwtToken !== undefined ? {'Authorization': 'Bearer ' + jwtToken} : {}}
}

export default jwtHeaders;