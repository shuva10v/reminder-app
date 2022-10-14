function jwtHeaders(jwtToken, additional={}) {
  return {headers: jwtToken !== undefined ? {...additional, ...{'Authorization': 'Bearer ' + jwtToken}} : additional}
}

export default jwtHeaders;