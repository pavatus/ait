{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
    systems.url = "github:nix-systems/default";
  };

  outputs = inputs:
    inputs.flake-parts.lib.mkFlake { inherit inputs; } {
      systems = import inputs.systems;
      perSystem = { config, self', pkgs, lib, system, ... }: {
      devShells.default = pkgs.mkShell {
        buildInpts = with pkgs; [
          zulu17
        ];
        LD_LIBRARY_PATH = with pkgs; lib.makeLibraryPath [
          libGL
          glfw
          openal
          flite
          libpulseaudio
          udev
          xorg.libXcursor
        ];
      };
    };
  };
}