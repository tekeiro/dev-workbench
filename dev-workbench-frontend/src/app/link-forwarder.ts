export const FollowLink = followLink;

function followLink(uri) {
  console.log('link to follow ' + uri);
  window.open(uri.toString(), '_blank');
}
